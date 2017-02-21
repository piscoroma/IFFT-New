package it.ifttt.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import it.ifttt.domain.User;
import it.ifttt.repository.UserRepository;

@Service
public class SimpleUserDetailsService implements UserDetailsService {

	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username);
		if(user==null)
			throw new UsernameNotFoundException("Error: user not found!");
		
		//get the encoded password
        //String encodedPassword = passwordEncoder.encode(user.getPassword());
        
        List<SimpleGrantedAuthority> authList = getAuthorities(user.getRole());
		
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authList);
	}
	
	private List<SimpleGrantedAuthority> getAuthorities(String role) {
        
		List<SimpleGrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
 
        //you can also add different roles here
        //for example, the user is also an admin of the site, then you can add ROLE_ADMIN
        //so that he can view pages that are ROLE_ADMIN specific
        if (role != null && role.trim().length() > 0) {
            if (role.equals("admin")) {
                authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        }
 
        return authList;
    }

}
