package jp.ne.takes.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jp.ne.takes.dto.AccountDto;

/**
 * アカウントDAOクラス
 * Feat.エンティティマネージャーjpql版
 * 
 * @author k_igari
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
@Repository("AccountDaoFeatEntityManagerJpql")  // (*2-4) 名前の設定
/** 全メソッドをトランザクション内で実行(読取り専用) */
@Transactional(readOnly = true)
public class AccountDaoFeatEntityManagerJpql implements AccountDao {
  /** EntityManagerのインスタンスをDI(注入) */
  @PersistenceContext
  private EntityManager entityManager;

  /**
   * メアドに対応するアカウントの取得
   *
   * @param email 取得するアカウントのメアド
   * @return メアドに対応するアカウント
   */
  @Override
  public Optional<AccountDto> findByEmail(String email) {
    // クエリの作成
    var jpql = "FROM AccountDto WHERE email = :email";
    TypedQuery<AccountDto> query = entityManager.createQuery(jpql, AccountDto.class);
    // パラメータの設定
    query.setParameter("email", email);
    // クエリの実行と結果の取得
    List<AccountDto> accounts = query.getResultList();
    return accounts.isEmpty() ? Optional.empty(): Optional.of(accounts.get(0));
  }

  /**
   * 全アカウント一覧の取得
   *
   * @return 全アカウント一覧
   */
  @Override
  public List<AccountDto> findAll() {
    // クエリの作成
    var jpql = "FROM AccountDto";
    var query = entityManager.createQuery(jpql, AccountDto.class);
    // クエリの実行と結果の取得
    var accounts = query.getResultList();
    return accounts;
  }

  /**
   * IDに対応するアカウントの取得
   *
   * @param id 取得するアカウントのID
   * @return IDに対応するアカウント
   */
  @Override
  public Optional<AccountDto> findById(Integer id) {
    // 主キーで検索し結果を取得
    AccountDto account = entityManager.find(AccountDto.class, id);
    return Optional.ofNullable(account);
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
    // クエリの作成
    var jpql = "FROM AccountDto WHERE email = :email AND id != :id";
    var query = entityManager.createQuery(jpql, AccountDto.class);
    // パラメータの設定
    query.setParameter("email", email);
    query.setParameter("id", id);
    // クエリの実行と結果の取得
    var accounts = query.getResultList();
    // メールアドレスが存在し、かつそのIDが指定されたIDと異なるかどうかを返す
    return !accounts.isEmpty();
  }

  /**
   * アカウントの更新
   * 
   * @param account 更新するアカウント
   */
  @Transactional(readOnly = false)
  @Override
  public void update(AccountDto account) {
    entityManager.merge(account);    
  }

  /**
   * IDに対応するアカウントの削除
   *
   * @param id 削除するアカウントのID
   */
  @Transactional(readOnly = false)
  @Override
  public void deleteById(Integer id) {
    var accountOpt = findById(id);
    entityManager.remove(accountOpt.get());
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
  @Transactional(readOnly = false)
  @Override
  public void create(AccountDto account) {
    entityManager.persist(account);
  }
}