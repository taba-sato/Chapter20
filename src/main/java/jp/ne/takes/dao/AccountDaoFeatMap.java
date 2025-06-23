package jp.ne.takes.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import jp.ne.takes.dto.AccountDto;

/**
 * アカウントDAOクラス
 * Feat.マップ
 * 
 * @author k_igari
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
@Repository("AccountDaoFeatMap")  // (*2-5.) 名前の設定 
public class AccountDaoFeatMap implements AccountDao {
  /**
   * 似非データベース※非永続的
   * ※ダブルブレース初期化は匿名内部クラスの生成、メモリリークの可能性、シリアル化の問題あり
   */
  private static Map<Integer, AccountDto> accounts = new ConcurrentHashMap<>() {{
    put(1, new AccountDto(1,"takes@takes.ne.jp","Takes200038"));
    put(2, new AccountDto(2, "gs_k.igari@takesgrp.info","Takes123456"));
    put(3, new AccountDto(3, "k.igari@takes.ne.jp","Takes123456"));
  }};

  /**
   * メアドに対応するアカウントの取得
   *
   * @param email 取得するアカウントのメアド
   * @return メアドに対応するアカウント
   */
  @Override
  public Optional<AccountDto> findByEmail(String email) {
    for (var account : accounts.values()) {
      if (account.getEmail().equals(email)) {
        return Optional.of(account);
      }
    }
    return Optional.empty();
  }

  /**
   * 全アカウント一覧の取得
   * 
   * @return 全アカウント一覧
   */
  @Override
  public List<AccountDto> findAll() {
    // mapの値をlistに変換
    return new ArrayList<>(accounts.values());
  }

  /**
   * IDに対応するアカウントの取得
   *
   * @param id 取得するアカウントのID
   * @return IDに対応するアカウント
   */
  @Override
  public Optional<AccountDto> findById(Integer id) {
    return Optional.ofNullable(accounts.get(id));
  }

  /**
   * 除外ID以外でメアドが存在するか確認
   * 
   * @param email 存在確認するメアド
   * @param id 除外するID
   * @return 除外ID以外でメアドが存在する場合は{@code true}/存在しない場合は{@code false}
   */
  @Override
  public boolean existsByEmailAndIdNot(String email, Integer id) {
    for (var account : accounts.values()) {
      if (!account.getEmail().equals(email)) {
        // メアドが異なる
        continue;
      }
      if (account.getId() == id) {
        // 除外ID
        continue;
      }
      // メアドが存在する
      return true;
    }
    // メアドが存在しない
    return false;
  }

  /**
   * アカウントの更新
   * 
   * @param account 更新するアカウント
   */
  @Override
  public void update(AccountDto account) {
    accounts.put(account.getId(), account);
  }

  /**
   * IDに対応するアカウントの削除
   *
   * @param id 削除するアカウントのID
   */
  @Override
  public void deleteById(Integer id) {
    accounts.remove(id);
  }

  /**
   * メアドが存在するか確認
   * 
   * @param email 存在確認するメアド
   * @return メアドが存在する場合は{@code true}/存在しない場合は{@code false}
   */
  @Override
  public boolean existsByEmail(String email) {
    return findByEmail(email).isPresent();
  }

  /**
   * アカウントの作成
   * 
   * @param account 作成するアカウント
   */
  @Override
  public void create(AccountDto account) {
    var allKeys = accounts.keySet();
    var newId = Collections.max(allKeys) + 1;
    account.setId(newId);
    accounts.put(newId, account);
  }
}