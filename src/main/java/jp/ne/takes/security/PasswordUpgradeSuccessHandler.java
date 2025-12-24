package jp.ne.takes.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jp.ne.takes.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
// noop→bcrypt変換用のコード。最終的に削除する予定のクラス
@RequiredArgsConstructor
public class PasswordUpgradeSuccessHandler implements AuthenticationSuccessHandler {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // 既定の遷移(直前の要求 or defaultSuccessUrl)を維持したいので委譲先を用意
    private final SavedRequestAwareAuthenticationSuccessHandler delegate =
            new SavedRequestAwareAuthenticationSuccessHandler();
    
    {
      // 元のURLが無ければ /home へ
      delegate.setDefaultTargetUrl("/home");
        }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // ログインに使った生パスワード（リクエストから直接取得）
        String raw = request.getParameter("password");
        String email = authentication.getName();  

     // DB から最新を再取得
        var account = accountRepository.findByEmail(email).orElse(null);
        if (account != null) {
            String stored = account.getPassword();
            if (stored != null && stored.startsWith("{noop}") && raw != null && !raw.isBlank()) {
                account.setPassword(passwordEncoder.encode(raw)); // {bcrypt}... で保存
                accountRepository.saveAndFlush(account);          
         }
        }
        // 既定の遷移ロジックに委譲（/homeや直前URLへ）
        delegate.onAuthenticationSuccess(request, response, authentication);
        System.out.println("upgraded");
    }
}
