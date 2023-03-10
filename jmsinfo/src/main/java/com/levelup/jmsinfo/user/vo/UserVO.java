package com.levelup.jmsinfo.user.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @purpose ?¬?©? ? λ³΄λ?? κ°?μ§?κ³? ?? κ°μ²΄
 * 
 * @  ?? ?Ό          ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.17       ? λ―Όμ       μ΅μ΄??±
 * @ 2023.02.22       ? λ―Όμ       ?¬?©? κΆν μΆκ?
 *
 * @author ? λ―Όμ
 * @since  2023.02.17
 *
 */
@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserVO implements UserDetails {

	private String userNo;
	private String userId;
	private String userPassword;
	private String userName;
	private List<GrantedAuthority> authorities;
	private boolean userEnabled;

	@Override
	public String getPassword() {
		return userPassword;
	}

	@Override
	public String getUsername() {
		return userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return userEnabled;
	}
	
	@Builder
    UserVO(String userNo, String userId, String userPassword, String userName, boolean userEnabled) {
        this.userNo = userNo;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEnabled = userEnabled;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
}

