package jp.ne.takes.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Chapter20:アカウントDAOクラスをインタフェースに変更
 * コントローラークラスの定義
 * 
 * @author k_igari  
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
@Controller
//@SessionAttributes("user") ←Spring Securityで自動的に認証情報はセッションに保持されるため不要
@RequiredArgsConstructor
@Slf4j
public class LoginController {
  /** アカウントサービス */
  //private final AccountService accountService; ←Securityで認証するため不要
  
  //Spring Security がログイン後に自動で認証情報をセッションに持つので不要
  /**
   * Userの生成
   * 
   * マッピングアノテーション付きのメソッド実行前に呼び出される
   * @return Userは自動的にモデルに追加
   */
//  @ModelAttribute("user")
//  public User createUser() {
//      return new User();
//  }

  /**
   * ハンドラーメソッド
   * URL: http://localhost:8080/
   * HTTPメソッド: GET
   *
   * @param session HttpSessionオブジェクト
   * @return "login" (ログイン画面を表示)
   */
  @GetMapping("/")
  public String index(@RequestParam(name = "error", required = false) String error, Model model) {
//    if (error != null) {
//      model.addAttribute("mesg", "メールアドレスまたはパスワードが間違っています");
//    }
//  login.htmlでエラー表示を実装
    return "login";
  }
  
/** Securityが処理するため不要 */
  /**
   * ハンドラーメソッド
   * URL: http://localhost:8080/login
   * HTTPメソッド: POST
   * 
   * @param accountDto AccountDtoオブジェクト
   * @param mdl Modelオブジェクト
   * @return "redirect:/home" (ホーム画面を表示)
   */
//  @PostMapping("/login")
//  public String login(@ModelAttribute User user, Model mdl) {
//    // メアドとパスワードを取得
//    var email = user.getEmail();
//    var password = user.getPassword();    
//    // メアドとパスワードを確認
//    if(!accountService.isLoginSuccessful(email, password)) {
//      // ログイン失敗、モデルにメッセージを設定
//      mdl.addAttribute("mesg", "メールアドレスまたはパスワードが間違っています");
//      // ビューにログイン画面を設定
//      return "login";
//    }
//    // ログイン成功、ホーム画面にリダイレクト 
//    return "redirect:/home";
//  }

  /**
   * ハンドラーメソッド
   * URL: http://localhost:8080/home
   * HTTPメソッド: GET
   *
   * @param mdl Modelオブジェクト
   * @return "home" (ホーム画面を表示) 
   */
  @GetMapping("/home")
  public String home(Model mdl) {
    // 日時のフォーマッターを取得
    var dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    var timeFormatter = DateTimeFormatter.ofPattern("HH時mm分ss秒");
    // フォーマットを適用した日時(文字列)を取得
    var date = LocalDate.now().format(dateFormatter);
    var time = LocalTime.now().format(timeFormatter);
    // モデルに日時を設定
    mdl.addAttribute("dateTime", Map.of("date", date, "time", time));
    // ビューにホーム画面を設定 
    return "home";
  } 
} // from Class
