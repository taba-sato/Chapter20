package jp.ne.takes.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * アカウントDTOクラス
 * アカウント新規作成・更新用
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
  private String email = "";

  /** パスワード */
  @Column
  private String password = "";
}