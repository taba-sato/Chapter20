package jp.ne.takes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ユーザークラス
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
  /** メールアドレス */
  private String email = "";
  /** パスワード */
  private String password = "";
}
