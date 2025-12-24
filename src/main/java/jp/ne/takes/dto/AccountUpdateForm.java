package jp.ne.takes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ユーザークラス
 * アカウントのEmail更新用DTO
 * 
 * @author k_igari
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
@NoArgsConstructor
@Getter
@Setter
public class AccountUpdateForm {
  /** ID */
  private int id = 0;
  /** メールアドレス */
  @NotBlank(message="メールアドレスが未入力です")
  @Email(message = "有効なメールアドレスを入力してください")
  private String email = "";
}
