package jp.ne.takes.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jp.ne.takes.dto.AccountDto;
import jp.ne.takes.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

/**
 * アカウントDAOクラス
 * Feat.JPAリポジトリ
 * 
 * @author k_igari
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
@Repository("AccountDaoFeatJpaRepository")  // (*2-2) 名前の設定
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountDaoFeatJpaRepository implements AccountDao {
  /** AccountRepositoryのインスタンスをDI(注入) */
  private final AccountRepository repository;

  /**
   * メアドに対応するアカウントの取得
   *
   * @param email 取得するアカウントのメアド
   * @return メアドに対応するアカウント
   */
  @Override
  public Optional<AccountDto> findByEmail(String email) {
    return repository.findByEmail(email);
  }

  /**
   * 全アカウント一覧の取得
   * @return 全アカウント一覧
   */
  @Override
  public List<AccountDto> findAll() {
    var accounts = repository.findAll();
    //var accounts = accountRepository.findAllByOrderByIdDesc();
    //var accounts = accountRepository.findAllByOrderByEmailAsc();
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
    return repository.findById(id);
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
    return repository.existsByEmailAndIdNot(email, id);
  }
  
  /**
   * アカウントの更新
   * 
   * @param account 更新するアカウント
   */
  @Transactional(readOnly = false)
  @Override
  public void update(AccountDto account) {
    repository.save(account);    
  }

  /**
   * IDに対応するアカウントの削除
   *
   * @param id 削除するアカウントのID
   */
  @Transactional(readOnly = false)
  @Override
  public void deleteById(Integer id) {
    repository.deleteById(id);    
  }

  /**
   * メアドが存在するか確認
   * 
   * @param email 存在確認するメアド
   * @return メアドが存在する場合は{@code true}/存在しない場合は{@code false}
   */
  @Override
  public boolean existsByEmail(String email) {
    return repository.existsByEmail(email);
  }

  /**
   * アカウントの作成
   * 
   * @param account 作成するアカウント
   */
  @Transactional(readOnly = false)
  @Override
  public void create(AccountDto account) {
    repository.save(account);    
  }
}