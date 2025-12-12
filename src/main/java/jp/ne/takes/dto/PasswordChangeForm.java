package jp.ne.takes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * パスワードチェンジフォームクラス
 * パスワード変更フォームDTO（本人だけパスワードを変更可）
 * 
 * @author s-tabara
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2025/12
 */
@NoArgsConstructor
@Getter
@Setter
public class PasswordChangeForm {

    @NotBlank(message = "現在のパスワードを入力してください")
    private String currentPassword = "";

    @NotBlank(message = "新しいパスワードを入力してください")
    @Size(min = 8, max = 12, message = "パスワードは8～12文字以内で入力してください")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$", message = "パスワードは大文字・小文字・数字を含めてください")
    private String newPassword = "";

    @NotBlank(message = "確認用パスワードを入力してください")
    private String confirmPassword = "";
}
