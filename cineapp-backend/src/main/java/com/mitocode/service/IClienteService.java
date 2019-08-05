package com.mitocode.service;

import java.util.List;

import com.mitocode.model.Cliente;

public interface IClienteService extends ICRUD<Cliente>{

	List<Cliente> buscarPorUsername(String username);
}
