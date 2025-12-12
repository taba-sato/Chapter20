package jp.ne.takes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jp.ne.takes.repository.AccountRepository;
import jp.ne.takes.security.AccountUserDetailsService;
import jp.ne.takes.security.PasswordUpgradeSuccessHandler;
import lombok.RequiredArgsConstructor;


/**
 * Spring Security のセキュリティ設定クラス。
 * 
 * このクラスでは以下の設定を行う：
 * <ul>
 *   <li>どのURLにアクセス制限をかけるか</li>
 *   <li>ログイン処理のページとURL</li>
 *   <li>ログアウト処理のURLとリダイレクト先</li>
 *   <li>ユーザー認証に使うサービスとパスワードの取り扱い</li>
 * </ul>
 * 
 * 現時点では開発用にパスワードは平文（NoOpPasswordEncoder）で処理。
 */
@Configuration // Spring による Java ベースの設定クラスであることを示す
@EnableWebSecurity // Spring Security を有効化する
@RequiredArgsConstructor
public class SecurityConfig {

  private final AccountUserDetailsService userDetailsService;
  
  @Bean
  public PasswordEncoder passwordEncoder() {
      // {bcrypt}, {noop}, {pbkdf2}, {scrypt} 等の接頭辞で自動判別
      return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
  
  /**
  * 認証プロバイダの定義。
  * 
  * 認証処理に使用する UserDetailsService と PasswordEncoder を指定。
  * 
  * @return DaoAuthenticationProviderのインスタンス
  */
  @Bean
  public DaoAuthenticationProvider authProvider() {
    var provider = new DaoAuthenticationProvider(); // 認証プロバイダーを作る
    provider.setUserDetailsService(userDetailsService); // ユーザー情報の取得方法を設定
    provider.setPasswordEncoder(passwordEncoder()); // パスワードの照合方法を設定
    return provider;  // Spring に登録
   }

  // 最終的に削除
  @Bean
  public PasswordUpgradeSuccessHandler passwordUpgradeSuccessHandler(
          AccountRepository accountRepository,
          PasswordEncoder passwordEncoder) {
      return new PasswordUpgradeSuccessHandler(accountRepository, passwordEncoder);
  }
  
  @Bean
  // SecurityFilterChain セキュリティルール（認可・ログイン・ログアウトなど）を定義
  public SecurityFilterChain filterChain(
       HttpSecurity http, 
       PasswordUpgradeSuccessHandler passwordUpgradeSuccessHandler) throws Exception {  
    http 
      .authorizeHttpRequests(auth -> auth // パスへのアクセス制御を行う
       // ログイン画面やCSSなどは誰でもアクセス可
          .requestMatchers("/", "/login", "/styles.css").permitAll()
          // その他すべてのURLは認証が必要
          .anyRequest().authenticated()
      )
      .formLogin(login -> login //ログイン画面設定
          // ログイン画面のパス（GET）
          .loginPage("/")
          // ログイン処理を行うパス（POST）
          .loginProcessingUrl("/login")
          .usernameParameter("username")
          .passwordParameter("password")
       // noop→bcrypt変換用のコード。最終的に削除する
          .successHandler(passwordUpgradeSuccessHandler)
          // 認証成功後に遷移するページ PasswordUpgradeSuccessHandlerクラス削除後に有効にする
          //.defaultSuccessUrl("/home", true)
          // 認証失敗時に遷移するURL
          .failureUrl("/?error")
          .permitAll()
      )
      .logout(logout -> logout
          // ログアウト処理を実行するURL
          .logoutUrl("/logout")
          // ログアウト後にリダイレクトするページ
          .logoutSuccessUrl("/")
          .invalidateHttpSession(true)
          .deleteCookies("JSESSIONID")
      );

  return http.build();
}

//  /**
//  * 開発中の暫定パスワードエンコーダ（平文で処理）。
//  * 
//  * 本番環境では必ず BCryptPasswordEncoder などに切り替えること。
//  * 
//  * @return NoOpPasswordEncoderのインスタンス
//  */
//  @SuppressWarnings("deprecation")
//  @Bean
//  public NoOpPasswordEncoder passwordEncoder() {
//    return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//  }



}