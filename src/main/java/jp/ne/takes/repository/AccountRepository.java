package jp.ne.takes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ne.takes.dto.AccountDto;

/**
 * アカウントリポジトリインタフェース
 * 
 * @author k_igari
 * @author Learning System Department
 * @author TAKES Co., Ltd.
 * @version 1.0 Date 2023/10
 */
public interface AccountRepository extends JpaRepository<AccountDto, Integer> {
  /**
   * メアドに対応するアカウントの取得
   *
   * @param email 取得するアカウントのメアド
   * @return メアドに対応するアカウント
   *  
   */
  Optional<AccountDto> findByEmail(String email);

  /**
   * メアドが存在するか確認
   * 
   * @param email 存在確認するメアド
   * @return メアドが存在する場合は{@code true}/存在しない場合は{@code false}
   */
  boolean existsByEmail(String email);

  /**
   * 除外ID以外でメアドが存在するか確認
   * 
   * @param email 存在確認するメアド
   * @param id 除外するID
   * @return 除外ID以外でメアドが存在する場合は{@code true}/存在しない場合は{@code false}
   */
  boolean existsByEmailAndIdNot(String email, Integer id);

  // 以下のメソッドはJpaRepositoryが宣言不要で提供

  // アカウントを保存（新規または更新）
  //AccountDto save(AccountDto account);

  // 複数のアカウントを保存
  //List<AccountDto> saveAll(Iterable<AccountDto> accounts);

  // IDに対応するアカウントの取得
  //Optional<AccountDto> findById(Integer id);

  // すべてのアカウントを検索
  //List<AccountDto> findAll();

  // すべてのアカウントをソート順で検索
  //List<AccountDto> findAll(Sort sort);

  // IDに対応するすべてのアカウントを取得
  //List<AccountDto> findAllById(Iterable<Integer> ids);

  // アカウントの総数をカウント
  //long count();

  // IDが存在するか確認
  //boolean existsById(Integer id);

  // IDに対応するアカウントの削除
  //void deleteById(Integer id);

  // 特定のアカウントを削除
  //void delete(AccountDto account);

  // すべてのアカウントを削除
  //void deleteAll();
}
