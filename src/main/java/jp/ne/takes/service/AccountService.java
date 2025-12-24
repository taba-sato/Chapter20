package jp.ne.takes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import jp.ne.takes.dao.AccountDao;
import jp.ne.takes.dto.AccountDto;
import jp.ne.takes.dto.AccountDto.Role;
import jp.ne.takes.dto.AccountUpdateForm;
import jp.ne.takes.dto.PasswordChangeForm;
import jp.ne.takes.dto.User;
import jp.ne.takes.security.AuthenticationRefresher;
import lombok.RequiredArgsConstructor;

/**
 * アカウントサービスクラスの定義
 * 
 * @author k_igari
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
@Service
@RequiredArgsConstructor
public class AccountService {
  /** (*2-1) アカウントDAO
   *  ※以下1つ有効にしたインスタンスをDI(注入)
   */
//  @Qualifier("AccountDaoFeatMap")
//  @Qualifier("AccountDaoFeatEntityManagerJpql")
//  @Qualifier("AccountDaoFeatEntityManagerSql")
  @Qualifier("AccountDaoFeatJpaRepository")
  private final AccountDao dao;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationRefresher authRefresher;
  
  /**
   * ログインの検証
   * 
   * @param email メールアドレス
   * @param password パスワード
   * @return 成功{@code true}/失敗{@code false}
   */
  public boolean isLoginSuccessful(String email, String password) {
    // アカウントをメアドで照会
    var accountOpt = dao.findByEmail(email);
    if (accountOpt.isEmpty()) {
      // アカウントが未登録
      return false;
    }
    var account = accountOpt.get();
    if (!account.getPassword().equals(password)) {
      // パスワードが不一致
      return false;
    }
    // メアドとパスワードが一致
    return true;
  }

  /**
   * 全アカウント一覧の取得
   * 
   * @return 全アカウント一覧
   */
  public List<AccountDto> findAll() {
    return dao.findAll();
  }  

  /**
   * IDに対応するアカウントの取得
   *
   * @param id 取得するアカウントのID
   * @return IDに対応するアカウント
   */
  public Optional<AccountDto> findById(int id) {
    return dao.findById(id);
  }

  /**
   * アカウントの更新
   * 
   * @param account 更新するアカウント情報
   * @param result バリデーションの結果
   * @return 成功{@code true}/失敗{@code false}
   */
  @Transactional
  public boolean isUpdateSuccessful(AccountUpdateForm account, BindingResult result) {
    // メアドの入力エラーを確認
    if(result.hasFieldErrors("email")) {
      // DBから元のEmailを取得、上書きして戻す
      var currentOpt = dao.findById(account.getId()).get();
      account.setEmail(currentOpt.getEmail());
      return false;
    }
    
    // ログイン中ユーザー（メール）を取得
    // SecurityContextHolder -> いまのリクエストを処理しているスレッドに紐づく“認証情報（だれがログイン中か）”の置き場所
    //  Spring Security がログイン成功時にここへ Authentication を入れ、各層（Controller/Service 等）から取り出せる
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String loginEmail = (auth != null ? auth.getName() : null);

    // 更新対象の現行レコードをDBから再取得
    var currentOpt = dao.findById(account.getId());
    if (currentOpt.isEmpty()) {
      result.reject("notfound", "対象アカウントが存在しません");
      return false;
    }
    var current = currentOpt.get();

    // 本人チェック：ログインメール と 現行レコードのメール が一致すること
    if (loginEmail == null || !loginEmail.equals(current.getEmail())) {
      result.reject("forbidden", "自分のアカウントのみ更新できます");
      return false;
    }
    
    // メアドが自身以外で使用されていないか確認
    if(dao.existsByEmailAndIdNot(account.getEmail(), account.getId())) {
      result.rejectValue("email", "error.email", "このメールアドレスは既に使用されています");
      return false;
    }

    // アカウント情報を更新
    current.setEmail(account.getEmail());
    dao.update(current);
    authRefresher.refreshIfSelf(account.getId());
    return true;
  }
  
  /**
   * パスワードの更新
   * 
   * @param email 更新するアカウントのemail
   * @param form  フォームで入力されたパスワード
   * @param result バリデーションの結果
   * @return 成功{@code true}/失敗{@code false}
   */
  @Transactional
  public boolean changeOwnPassword(String email,
                                   PasswordChangeForm form,
                                   BindingResult result) {
    // 本人のレコードを取得
    var accountOpt = dao.findByEmail(email);
    if (accountOpt.isEmpty()) {
      result.reject("notfound", "アカウントが見つかりません");
      return false;
    }
    var account = accountOpt.get();

    // 現在パスワードの一致チェック(フォームで入力した値とDBから取得した値比較)
    if (!passwordEncoder.matches(form.getCurrentPassword(), account.getPassword())) {
      result.rejectValue("currentPassword", "mismatch.current", "現在のパスワードが正しくありません");
      return false;
    }

    // 新パスワード一致＆差分チェック
    if (!form.getNewPassword().equals(form.getConfirmPassword())) {
      result.rejectValue("confirmPassword", "mismatch.confirm", "確認用パスワードが一致しません");
      return false;
    }
    if (passwordEncoder.matches(form.getNewPassword(), account.getPassword())) {
      result.rejectValue("newPassword", "same.as.old", "現在のパスワードと同一です");
      return false;
    }

    // ハッシュ化して保存
    account.setPassword(passwordEncoder.encode(form.getNewPassword()));
    dao.update(account); 
    return true;
  }

  /**
   * IDに対応するアカウントの削除
   *
   * @param id 削除するアカウントのID
   */
  public void deleteById(int id) {
    dao.deleteById(id);
  }

  /**
   * アカウントの登録
   * 
   * @param account 登録するアカウント情報
   * @param result バリデーションの結果
   * @return 成功{@code true}/失敗{@code false}
   */
  @Transactional
  public boolean isRegisterSuccessful(User user, BindingResult result) {
    // メアドの入力エラーを確認
    if(result.hasFieldErrors("email")) {
      return false;
    }
    // メアドが使用されていないか確認
    if(dao.existsByEmail(user.getEmail())) {
      result.rejectValue("email", "error.email", "このメールアドレスは既に使用されています");
      return false;
    }
    // パスワードの入力エラーを確認
    if(result.hasFieldErrors("password")) {
      return false;
    }
    //ハッシュ化
    var encoded = passwordEncoder.encode(user.getPassword());

    // 永続化用エンティティに詰め替え
    var account = new AccountDto();
    account.setEmail(user.getEmail());
    account.setPassword(encoded);
    account.setRole(Role.USER); // デフォルトで権限をUSERにする
    // アカウントを作成
    dao.create(account);
    return true;
  }
} // from Class
