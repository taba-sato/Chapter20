package jp.ne.takes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ユーザークラス
 * 入力用フォームDTO（登録・更新の入力検証用）
 * 
 * @author k_igari
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
@NoArgsConstructor
@Getter
@Setter
public class User {
  /** ID */
  private int id = 0;
  /** メールアドレス */
  @NotBlank(message="メールアドレスが未入力です")
  @Email(message = "有効なメールアドレスを入力してください")
  private String email = "";
  /** パスワード */
  @NotBlank(message="パスワードが未入力です")
  @Size(min = 8, max = 12, message = "パスワードは8～12文字以内で入力してください")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$", message = "パスワードは大文字・小文字・数字を含めてください")
  private String password = "";
}
