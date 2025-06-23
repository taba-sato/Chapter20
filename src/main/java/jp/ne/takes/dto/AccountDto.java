package jp.ne.takes.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * アカウントDTOクラス
 * 
 * @author k_igari
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
/** データベースにマッピング */
@Entity
@Table(name="accounts")
/** コンストラクタとアクセサを自動生成 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDto {
  /** ID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private int id = 0;

  /** メールアドレス */
  @Column
  @NotBlank(message="メールアドレスが未入力です")
  @Email(message = "有効なメールアドレスを入力してください")
  private String email = "";

  /** パスワード */
  @Column
  @NotBlank(message="パスワードが未入力です")
  @Size(min = 8, max = 12, message = "パスワードは8～12文字以内で入力してください")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$", message = "パスワードは大文字・小文字・数字を含めてください")
  private String password = "";
}