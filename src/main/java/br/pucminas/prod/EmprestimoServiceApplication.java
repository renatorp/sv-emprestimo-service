package br.pucminas.prod;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import br.pucminas.prod.entity.Cliente;
import br.pucminas.prod.entity.Emprestimo;
import br.pucminas.prod.entity.ItemEmprestimo;

@RestController
@SpringBootApplication
@RequestMapping(value = "/emprestimos")
public class EmprestimoServiceApplication {
	
	RestTemplate restTemplate = new RestTemplate();
	
	private static int nextId = 1;
	private static Map<Integer, Emprestimo> poolEmprestimos = new HashMap<>();
	
	public static void main(String[] args) {
		SpringApplication.run(EmprestimoServiceApplication.class, args);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Emprestimo confirmarEmprestimo(@RequestParam String idItem, @RequestParam String idSolicitante ) {
		
	    Cliente solicitante = restTemplate.getForObject("http://localhost:8080/clientes/{id}", Cliente.class, idSolicitante);
	    ItemEmprestimo item = restTemplate.getForObject("http://localhost:8081/itens/{id}", ItemEmprestimo.class, idItem);
	    
	    Emprestimo emprestimo = new Emprestimo();
	    
	    if (item != null && solicitante != null) {
	    	
	    	// Marca item como indisponível
	    	item.setDisponivel(false);
	    	restTemplate.put("http://localhost:8081/itens/", item);
	    	
	    	//Cria empréstimo		
	    	emprestimo.setId(nextId++);
	    	emprestimo.setSolicitante(solicitante);
	    	emprestimo.setItem(item);
	    	emprestimo.setFinalziado(false);
	    	
	    	//Salva
	    	poolEmprestimos.put(emprestimo.getId(), emprestimo);
	    	
	    	return emprestimo;
	    }
	    return null;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public Emprestimo finalizarEmprestimo(@PathVariable Integer id) {
		
		// Marca empréstimo como finalizado
		Emprestimo emprestimo = poolEmprestimos.get(id);
		emprestimo.setFinalziado(true);
		
		// Marca item como disponível
		ItemEmprestimo item = restTemplate.getForObject("http://localhost:8081/itens/{id}", ItemEmprestimo.class, emprestimo.getItem().getId());
		item.setDisponivel(true);
		restTemplate.put("http://localhost:8081/itens/", item);
		
		return emprestimo;
	}

}
