package com.whiterabbit.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JWTAuthorisationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //ajout token dans header via authorisation
        //response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Request-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, authorization");
        response.addHeader("Access-Control-Allow-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");
        //autoriser request cors put, delete
        response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,PATCH,OPTIONS");

        //si requete envoyé avec OPTION pas de recherche de token on repond OK car pas encore de token
        if(request.getMethod().equals("OPTIONS")){
            System.out.println("OPTIONS : ");
            response.setStatus(HttpServletResponse.SC_OK);
            //pas de recherche tokken non plus car accès page pour s'autentifier pas encore de token
        }
        //sinon recherche du token
       else {

             //recup token JWT passé dans la requette client
            String jwtToken = request.getHeader(SecurityParams.JWT_HEADER_NAME);
            System.out.println("Token JWT : " + jwtToken);
            if (jwtToken == null || !jwtToken.startsWith(SecurityParams.HEADER_PREFIX)) {
                //passage au filtre suivant
                System.out.println("*****Passage dans JWTFilter 0*****");
                filterChain.doFilter(request, response);
                return;
            }

            //vérifier la signature du token (sans le prefix)
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken.substring(SecurityParams.HEADER_PREFIX.length()));

            //recup user et roles dans le token (pas besoin accès base les roles sont dans le token dans notre cas)
            String username = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);
            System.out.print("username : " + username);
            System.out.print("roles : " + roles);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(roleName -> {
                authorities.add(new SimpleGrantedAuthority(roleName));
            });

            //Spring authentifie le user
            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(user);

            //passer au filtre suivant
            System.out.println("*****Passage dans JWTFilter*****");
            filterChain.doFilter(request, response);
        }
    }
}
