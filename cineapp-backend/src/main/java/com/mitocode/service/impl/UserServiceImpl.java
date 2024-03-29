package com.mitocode.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitocode.model.Cliente;
import com.mitocode.model.Usuario;
import com.mitocode.repo.IClienteRepo;
import com.mitocode.repo.IUsuarioRepo;
import com.mitocode.service.IUsuarioService;

@Service
public class UserServiceImpl implements UserDetailsService, IUsuarioService {
	
	@Autowired
	private IUsuarioRepo userRepo;
	@Autowired
	private IClienteRepo clienteRepo;
	
	@Value("${mitocine.default-rol}")
	private Integer DEFAULT_ROL;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = userRepo.findOneByUsername(username); //from usuario where username := username
		
		if (user == null) {
			throw new UsernameNotFoundException(String.format("Usuario no existe", username));
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		user.getRoles().forEach( role -> {
			authorities.add(new SimpleGrantedAuthority(role.getNombre()));
		});
		
		UserDetails userDetails = new User(user.getUsername(), user.getPassword(), authorities);
		
		return userDetails;
	}

	@Transactional
	@Override
	public Usuario registrarTransaccional(Usuario usuario) {	
		Usuario u;
		try {
			u = userRepo.save(usuario);	
			userRepo.registrarRolPorDefecto(u.getIdUsuario(), DEFAULT_ROL);	
		}catch(Exception e) {
			throw e;
		}
		
		return u;
		
	}

	@Transactional
	@Override
	public Usuario registrar(Usuario t) {
		Usuario n;
		n = userRepo.save(t);
		Cliente c = n.getCliente();
		if(c.getFoto().length > 0) {
			clienteRepo.modificarFoto(c.getIdCliente(), c.getFoto());			
		}	

		userRepo.registrarRolPorDefecto(n.getIdUsuario(), DEFAULT_ROL);	
		return n;
	}

	@Transactional
	@Override
	public Usuario modificar(Usuario t) {
		Cliente c = t.getCliente();
		if(c.getFoto().length > 0) {
			clienteRepo.modificarFoto(c.getIdCliente(), c.getFoto());			
		}	
		return userRepo.save(t);
	}

	@Override
	public List<Usuario> listar() {
		return userRepo.findAll();
	}

	@Override
	public Usuario leer(Integer id) {
		Optional<Usuario> op = userRepo.findById(id);
		return op.isPresent() ? op.get() : new Usuario();
	}

	@Override
	public void eliminar(Integer id) {
		clienteRepo.deleteById(id);
		//userRepo.deleteById(id);
		
	}

	@Override
	public Page<Usuario> listarPageable(Pageable pageable) {
		return userRepo.findAll(pageable);
	}

}
