package jp.ne.takes.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jp.ne.takes.dto.AccountDto;
import lombok.RequiredArgsConstructor;

/**
 * アカウントDAOクラス
 * Feat.エンティティマネージャーsql版
 * 
 * @author k_igari
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
@Repository("AccountDaoFeatEntityManagerSql")  // (*2-3) 名前の設定
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class AccountDaoFeatEntityManagerSql  implements AccountDao{
  /** EntityManagerのインスタンスをDI(注入) */
  @PersistenceContext
  private EntityManager entityManager;
  /** PlatformTransactionManagerのインスタンスをDI(注入) */
  private final PlatformTransactionManager transactionManager;

  /**
   * メアドに対応するアカウントの取得
   *
   * @param email 取得するアカウントのメアド
   * @return メアドに対応するアカウント
   */
  @Transactional(readOnly = true)
  @Override
  public Optional<AccountDto> findByEmail(String email) {
    // クエリの作成
    var sql = "SELECT * FROM accounts WHERE email = ?";
    Query query = entityManager.createNativeQuery(sql, AccountDto.class);
    // パラメータの設定
    query.setParameter(1, email);
    // クエリの実行と結果の取得
    List<AccountDto> accounts = query.getResultList();
    return accounts.isEmpty() ? Optional.empty(): Optional.of(accounts.get(0));
  }

  /**
   * 全アカウント一覧の取得
   *
   * @return 全アカウント一覧
   */
  @Transactional(readOnly = true)
  @Override
  public List<AccountDto> findAll() {
    // クエリの作成
    var sql = "SELECT * FROM accounts";
    Query query = entityManager.createNativeQuery(sql, AccountDto.class);
    // クエリの実行と結果の取得
    List<AccountDto> accounts = query.getResultList();
    return accounts;
  }

  /**
   * IDに対応するアカウントの取得
   *
   * @param id 取得するアカウントのID
   * @return IDに対応するアカウント
   */
  @Transactional(readOnly = true)
  @Override
  public Optional<AccountDto> findById(Integer id) {
    // クエリの作成
    var sql = "SELECT * FROM accounts WHERE id = ?";
    Query query = entityManager.createNativeQuery(sql, AccountDto.class);
    // パラメータの設定
    query.setParameter(1, id);
    // クエリの実行と結果の取得
    List<AccountDto> accounts = query.getResultList();
    return accounts.isEmpty() ? Optional.empty(): Optional.of(accounts.get(0));
  }

  /**
   * 除外ID以外でメアドが存在するか確認
   * 
   * @param email 存在確認するメアド
   * @param id 除外するID
   * @return 除外ID以外でメアドが存在する場合は{@code true}/存在しない場合は{@code false}
   */
  @Transactional(readOnly = true)
  @Override
  public boolean existsByEmailAndIdNot(String email, Integer id) {
    // クエリの作成
    String sql = "SELECT * FROM accounts WHERE email = ? AND id != ?";
    Query query = entityManager.createNativeQuery(sql, AccountDto.class);
    // パラメータの設定
    query.setParameter(1, email);
    query.setParameter(2, id);
    // クエリの実行と結果の取得
    List<AccountDto> accounts = query.getResultList();
    // メールアドレスが存在し、かつそのIDが指定されたIDと異なるかどうかを返す
    return !accounts.isEmpty();
  }

  /**
   * アカウントの更新
   * アノテーション@Transactionalを使わずに処理
   * 
   * @param account 更新するアカウント
   */
  @Override
  public void update(AccountDto account) {
    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
    try {
      // クエリの作成
      var sql = "UPDATE accounts SET email = ?, password = ? WHERE id = ?";
      Query query = entityManager.createNativeQuery(sql);
      // パラメータの設定
      query.setParameter(1, account.getEmail());
      query.setParameter(2, account.getPassword());
      query.setParameter(3, account.getId());
      // クエリの実行
      query.executeUpdate();
      // コミット
      transactionManager.commit(status);
    } catch (Exception e) {
      // 例外発生時はロールバック
      transactionManager.rollback(status);
      e.printStackTrace();
    }
  }

  /**
   * IDに対応するアカウントの削除
   *
   * @param id 削除するアカウントのID
   */
  @Transactional(readOnly = false)
  @Override
  public void deleteById(Integer id) {
    // クエリの作成
    var sql = "DELETE FROM accounts WHERE id = ?";
       Query query = entityManager.createNativeQuery(sql);
    // パラメータの設定
    query.setParameter(1, id);
    // クエリの実行
    query.executeUpdate();
  }

  /**
   * メアドが存在するか確認
   * 
   * @param email 存在確認するメアド
   * @return メアドが存在する場合は{@code true}/存在しない場合は{@code false}
   */
  @Transactional(readOnly = true)
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
    // クエリの作成
    var sql = "INSERT INTO accounts (email, password) VALUES (?, ?)";
    Query query = entityManager.createNativeQuery(sql);
    // パラメータの設定
    query.setParameter(1, account.getEmail());
    query.setParameter(2, account.getPassword());
    // クエリの実行
    query.executeUpdate();
  }
}