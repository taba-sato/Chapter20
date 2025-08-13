package jp.ne.takes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import jp.ne.takes.dao.AccountDao;
import jp.ne.takes.dto.AccountDto;
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

  @Autowired
  private PasswordEncoder passwordEncoder;
  
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
  public boolean isUpdateSuccessful(AccountDto account, BindingResult result) {
    // メアドの入力エラーを確認
    if(result.hasFieldErrors("email")) {
      return false;
    }
    // メアドが自身以外で使用されていないか確認
    if(dao.existsByEmailAndIdNot(account.getEmail(), account.getId())) {
      result.rejectValue("email", "error.email", "このメールアドレスは既に使用されています");
      return false;
    }
    // パスワードの入力エラーを確認
    if(result.hasFieldErrors("password")) {
      return false;
    }
    // アカウント情報を更新
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
  public boolean isRegisterSuccessful(AccountDto account, BindingResult result) {
    // メアドの入力エラーを確認
    if(result.hasFieldErrors("email")) {
      return false;
    }
    // メアドが使用されていないか確認
    if(dao.existsByEmail(account.getEmail())) {
      result.rejectValue("email", "error.email", "このメールアドレスは既に使用されています");
      return false;
    }
    // パスワードの入力エラーを確認
    if(result.hasFieldErrors("password")) {
      return false;
    }
    // パスワードを暗号化して上書き
    account.setPassword(passwordEncoder.encode(account.getPassword()));
    // アカウントを作成
    dao.create(account);
    return true;
  }
} // from Class
