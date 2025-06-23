package jp.ne.takes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jp.ne.takes.dto.AccountDto;
import jp.ne.takes.service.AccountService;
import lombok.RequiredArgsConstructor;

/**
 * アカウントコントローラークラスの定義
 * 
 * @author k_igari  
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
@Controller
@RequiredArgsConstructor
public class AccountController {
  /** アカウントサービス */
  private final AccountService accountService;
 
  /**
   * ハンドラーメソッド 
   * URL: http://localhost:8080/account-list
   * HTTPメソッド: GET
   *
   * @param mdl Modelオブジェクト
   * @return "account-list"（アカウント一覧画面を表示）
   */
  @GetMapping("/account/list")
  public String list(Model mdl) {
    // アカウント一覧を取得
    var list = accountService.findAll();
    // モデルにアカウント一覧を設定
    mdl.addAttribute("accountList",list);
    // ビューにアカウント一覧画面を設定 
    return "account-list";
  }

  /**
   * ハンドラーメソッド 
   * URL: http://localhost:8080/account/id
   * HTTPメソッド: GET
   * 指定されたidのアカント更新画面を表示
   *
   * @param id /account/{id}から取得
   * @param mdl Modelオブジェクト
   * @return "account-form"（アカウント更新画面を表示）
   */
  @GetMapping("/account/{id}")
  public String byId(@PathVariable(name = "id") int id, Model mdl) {
    // IDでアカウントを取得   
    var accountOpt = accountService.findById(id);
    mdl.addAttribute("accountDto", accountOpt.get());
    return "account-form";
  }

  /**
   * アカント情報の更新：ハンドラーメソッド 
   * URL: http://localhost:8080/account/update
   * HTTPメソッド: POST
   *
   * @param accountDto 更新するアカウント情報
   * @param result バリデーションの結果
   * @return 成功："redirect:/account-list"（アカウント一覧画面を表示）
   *         失敗："account-form"（アカウント更新画面を表示）
   */
  @PostMapping("/account/update")
  public String update(@ModelAttribute @Validated AccountDto account, BindingResult result) {
    if (!accountService.isUpdateSuccessful(account, result)) {
      // アカウントの更新に失敗
      return "account-form";
    }
    // アカウント一覧にリダイレクト
    return "redirect:/account/list";
  }
  
  /**
   * アカウント更新のキャンセル：ハンドラーメソッド 
   * URL: http://localhost:8080/account/cancel
   * HTTPメソッド: POST
   *
   * @return "redirect:/account-list"（アカウント一覧画面を表示）
   */
  @PostMapping("/account/cancel")
  public String cancel() {
    // アカウント一覧にリダイレクト 
    return "redirect:/account/list";
  }

  /**
   * アカウントの削除：ハンドラーメソッド 
   * URL: http://localhost:8080/account/delete
   * HTTPメソッド: POST
   *
   * @return "redirect:/account-list"（アカウント一覧画面を表示）
   */
  @PostMapping("/account/delete")
  public String delete(@ModelAttribute AccountDto account) {
    accountService.deleteById(account.getId());
    return "redirect:/account/list";
  }

  /**
   * アカウントの作成：ハンドラーメソッド
   * URL: http://localhost:8080/account/create
   * HTTPメソッド: GET
   *
   * @return "account-register"（新規登録画面を表示）
   */
  @GetMapping("/account/create")
  public String create(Model mdl) {
    // 新規アカウントを生成しモデルに設定
    mdl.addAttribute("accountDto", new AccountDto());
    return "account-register";
  }

  /**
   * アカウントの登録：ハンドラーメソッド
   * URL: http://localhost:8080/account/register
   * HTTPメソッド: POST
   *
   * @param account 登録するアカウント情報
   * @param result バリデーションの結果
   * @return 成功："account-list"（アカウント一覧画面を表示）
   *         失敗："account-register"（新規登録画面を表示）
   */
  @PostMapping("/account/register")
  public String register(@ModelAttribute @Validated AccountDto account, BindingResult result) {
    // アカウントの登録
    if(accountService.isRegisterSuccessful(account, result)) {
      // 成功
      return "redirect:/account/list";
    }
    // 失敗
    return "account-register";
  }
}
