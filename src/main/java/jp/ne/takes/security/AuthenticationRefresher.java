package jp.ne.takes.security;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jp.ne.takes.dao.AccountDao;
import lombok.RequiredArgsConstructor;

/**
 * 認証情報リフレッシャ。
 *
 * メールアドレス変更後に、現在のリクエストスレッドに紐づく
 * SecurityContext の Authentication を、最新の AccountPrincipal へ差し替える
 *
 * これにより、ビューの #authentication.name が即時に更新内容を反映。
 */
@Component
@RequiredArgsConstructor
public class AuthenticationRefresher {

//  @Qualifier("AccountDaoFeatMap")
//  @Qualifier("AccountDaoFeatEntityManagerJpql")
//  @Qualifier("AccountDaoFeatEntityManagerSql")
  @Qualifier("AccountDaoFeatJpaRepository")
  private final AccountDao dao;

  /**
   * 引数のアカウントIDが「現在ログイン中の本人」の場合に限って、
   * SecurityContext の Authentication を最新の principal で差し替える。
   *
   * @param id 更新したアカウントの ID
   */
  @Transactional
  public void refreshIfSelf(int id) {
      // 今のスレッドに紐づく認証状態(SecurityContext)を取得
      var context = SecurityContextHolder.getContext();
      // 現在の認証トークン（Authentication）を取得
      var currentAuth = context.getAuthentication();
      // 未ログインなら何もせず終了
      if (currentAuth == null) return;

      // ログイン中のアカウント情報(Principal)を取得
      var principal = currentAuth.getPrincipal();
      // 自作の AccountPrincipal でない場合（例えば匿名や別の実装）には触れず終了
      if (!(principal instanceof AccountPrincipal ap)) return;

      // 本人か判定（現在のprincipalのIDと更新対象IDの一致を確認）
      if (ap.getAccount().getId() != id) return;

      // DBから最新状態を再取得（更新直後の値を反映）
      var updatedOpt = dao.findById(id);
      if (updatedOpt.isEmpty()) return;

      // 新しいPrincipalを作成してセッション内の本人情報を最新化
      var newPrincipal = new AccountPrincipal(updatedOpt.get());

      // 資格情報(認証に使ったパスワード)、既存の権限・details(IPアドレス、セッションID等) を維持したまま principal だけ置き換える
      var newAuth = new UsernamePasswordAuthenticationToken(
              newPrincipal,
              currentAuth.getCredentials(), //資格情報
              currentAuth.getAuthorities() //権限情報
      );
      newAuth.setDetails(currentAuth.getDetails());

      // SecurityContext に反映
      context.setAuthentication(newAuth);
  }
}
