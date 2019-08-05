package com.mitocode.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mitocode.model.Cliente;

public interface IClienteRepo extends JpaRepository<Cliente, Integer>{
	
	@Query("from Cliente c where c.usuario.username =:username")
	List<Cliente> buscarPorUsername(@Param("username")String username);
	
	
	@Modifying
	@Query("UPDATE Cliente set foto = :foto where id = :id")
	void modificarFoto(@Param("id") Integer id, @Param("foto") byte[] foto);
}
