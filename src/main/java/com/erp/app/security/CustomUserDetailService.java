 package com.erp.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.erp.app.entities.Users;
import com.erp.app.repository.UserRepo;
import com.google.common.base.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Users user = userRepo.findByEmail(email);
//        
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with email: " + email);
//        }
//
//        return new org.springframework.security.core.userdetails.User(
//            user.getEmail(),
//            user.getPassword(),
//            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
//        );
//    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	Optional<Users> userOptional = userRepo.findByEmail(email);

    	if (userOptional.isPresent()) {
    	    Users user = userOptional.get();
    	    // Proceed with your logic
    	    return new CustomUserDetails(user);
    	} else {
    	    // Handle the "null" case gracefully
    	    // e.g., return null, log a message, or return a guest user
    	    return null; 
    	}

       
    }

//    // Optional helper if needed elsewhere
    public Users loadUserDetails(String email)   {
        Users user = userRepo.findByEmail(email).orNull();
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return user;
    }
}

