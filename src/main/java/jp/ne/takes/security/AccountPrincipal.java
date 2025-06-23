package jp.ne.takes.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jp.ne.takes.dto.AccountDto;
import lombok.RequiredArgsConstructor;

/**
 * Spring Security の UserDetails 実装クラス。
 * 
 * 認証に使用するユーザー情報（アカウント情報）をラップして、
 * Spring Security が必要とする形式で提供する。
 * 
 * 
 * 本クラスは {@link AccountDto} を元に、メールアドレス・パスワードなどを
 * {@code UserDetails} インタフェース経由で Spring Security に提供する。
 * 権限（ロール）は現時点では未使用のため空リストを返す。
 * 
 * @author TAKES Co., Ltd.
 */
@RequiredArgsConstructor
public class AccountPrincipal implements UserDetails {

    /** アカウント情報（メールアドレス・パスワード）を保持するDTO */
    private final AccountDto account;

    /**
     * 権限情報を返す（未使用のため空リストを返却）
     * 
     * @return 空の権限リスト
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * パスワードを取得する
     * 
     * @return パスワード（現在は平文）
     */
    @Override
    public String getPassword() {
        return account.getPassword();
    }

    /**
     * ユーザー名（メールアドレス）を取得する
     * 
     * @return メールアドレス
     */
    @Override
    public String getUsername() {
        return account.getEmail();
    }

    /**
     * アカウントの有効期限が切れていないかを判定
     * 
     * @return 常に {@code true}
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * アカウントがロックされていないかを判定
     * 
     * @return 常に {@code true}
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 認証情報の有効期限が切れていないかを判定
     * 
     * @return 常に {@code true}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * アカウントが有効であるかを判定
     * 
     * @return 常に {@code true}
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
