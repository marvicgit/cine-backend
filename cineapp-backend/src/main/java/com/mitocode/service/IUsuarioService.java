package com.mitocode.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mitocode.model.Usuario;

public interface IUsuarioService extends ICRUD<Usuario>{

	Usuario registrarTransaccional(Usuario us);
	Page<Usuario> listarPageable(Pageable pageable);
}
