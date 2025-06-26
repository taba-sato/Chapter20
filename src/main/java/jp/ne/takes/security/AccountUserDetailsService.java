package jp.ne.takes.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.ne.takes.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

/**
 * Spring Security の UserDetailsService 実装クラス。
 * 
 * ログイン時に入力されたメールアドレスをもとに、
 * アカウント情報（AccountDto）をデータベースから取得し、
 * {@link AccountPrincipal} にラップして返す。
 * 
 * 
 * 本クラスは {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}
 * から呼び出され、ログイン認証の対象となるユーザー情報を提供する。
 * 
 * 
 * @author TAKES Co., Ltd.
 */
@Service
@RequiredArgsConstructor
public class AccountUserDetailsService implements UserDetailsService {

    /** アカウント情報を取得するためのリポジトリ */
    private final AccountRepository repository;

    /**
     * メールアドレスをもとにユーザー情報を取得する。
     * 
     * @param email 入力されたメールアドレス（ユーザー名）
     * @return Spring Security 用のユーザー情報（AccountPrincipal）
     * @throws UsernameNotFoundException アカウントが見つからなかった場合にスロー
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .map(AccountPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが存在しません: " + email));
    }
}
