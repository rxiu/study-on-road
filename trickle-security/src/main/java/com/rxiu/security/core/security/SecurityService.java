package com.rxiu.security.core.security;

import com.rxiu.security.core.BeanFactory;
import com.rxiu.security.web.mapper.AuthorityMapper;
import com.rxiu.security.web.mapper.UserMapper;
import com.rxiu.security.web.model.Authority;
import com.rxiu.security.web.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rxiu on 2018/3/19.
 */
@Service
public class SecurityService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                BeanFactory.getBean(UserMapper.class).getByUsername(username);

        if (user != null) {
            List<Authority> authorities =
                    BeanFactory.getBean(AuthorityMapper.class).getAuthorityByUserId(user.getId());

            user.setAuthorities(authorities);

            authorities.forEach(authority -> authority = null);
            authorities = null;

            return user;
        } else {
            throw new UsernameNotFoundException("error! " + username + " do not exist!");
        }
    }
}
