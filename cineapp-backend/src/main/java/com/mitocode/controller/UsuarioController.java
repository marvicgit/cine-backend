package com.mitocode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

import com.mitocode.model.Cliente;
import com.mitocode.model.Usuario;
import com.mitocode.service.IUsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private IUsuarioService service;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@PostMapping(produces = "application/json", consumes = "application/json")
	private ResponseEntity<Object> registrar(@RequestBody Usuario usuario){		
		usuario.setPassword(bcrypt.encode(usuario.getPassword()));
		service.registrarTransaccional(usuario);
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Usuario>> listar(){
		List<Usuario> lista = service.listar();
		return new ResponseEntity<List<Usuario>>(lista, HttpStatus.OK);		
	}

	@GetMapping("/{id}")
	public Usuario leer(@PathVariable("id") Integer id) {
		return service.leer(id);
	}

	@PostMapping
	public Usuario registrar(@RequestPart("usuario") Usuario usuario, @RequestPart("file") MultipartFile file) throws IOException {
		Usuario u = usuario;
		u.setPassword(bcrypt.encode(usuario.getPassword()));
		Cliente c = u.getCliente();
		c.setFoto(file.getBytes());
		u.setCliente(c);
		return service.registrar(u);
	}
	

	@PutMapping
	public Usuario modificar(@RequestPart("usuario") Usuario usuario, @RequestPart("file") MultipartFile file) throws IOException {
		Usuario u = usuario;
		if ((usuario.getPassword() != null) && (!usuario.getPassword().equals(""))) {
		u.setPassword(bcrypt.encode(usuario.getPassword()));
		} else {
			u.setPassword(service.leer(u.getIdUsuario()).getPassword());
		}
		Cliente c = u.getCliente();
		c.setFoto(file.getBytes());
		u.setCliente(c);
		return service.modificar(u);
	}

	@GetMapping(value="/pageable", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Usuario>> listarPageable(Pageable pageable){
		Page<Usuario> pacientes;
		pacientes = service.listarPageable(pageable);
		return new ResponseEntity<Page<Usuario>>(pacientes, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
	}
}
