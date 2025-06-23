package jp.ne.takes.dao;

import java.util.List;
import java.util.Optional;

import jp.ne.takes.dto.AccountDto;

/**
 * (*1) アカウントDAOインタフェース
 * 
 * @author k_igari
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
public interface AccountDao {
  /**
   * メアドに対応するアカウントの取得
   *
   * @param email 取得するアカウントのメアド
   * @return メアドに対応するアカウント
   */
   public Optional<AccountDto> findByEmail(String email);

  /**
   * 全アカウント一覧の取得
   * @return 全アカウント一覧
   */
  public List<AccountDto> findAll();

  /**
   * IDに対応するアカウントの取得
   *
   * @param id 取得するアカウントのID
   * @return IDに対応するアカウント
   */
  public Optional<AccountDto> findById(Integer id);

  /**
   * 除外ID以外でメアドが存在するか確認
   * 
   * @param email 存在確認するメアド
   * @param id 除外するID
   * @return 除外ID以外でメアドが存在する場合は{@code true}/存在しない場合は{@code false}
   */
  public boolean existsByEmailAndIdNot(String email, Integer id);

  /**
   * アカウントの更新
   * 
   * @param account 更新するアカウント
   */
  public void update(AccountDto account);

  /**
   * IDに対応するアカウントの削除
   *
   * @param id 削除するアカウントのID
   */
  public void deleteById(Integer id);

  /**
   * メアドが存在するか確認
   * 
   * @param email 存在確認するメアド
   * @return メアドが存在する場合は{@code true}/存在しない場合は{@code false}
   */
  public boolean existsByEmail(String email);

  /**
   * アカウントの作成
   * 
   * @param account 作成するアカウント
   */
 public void create(AccountDto account);
}
