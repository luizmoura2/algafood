package com.algaworks.algafood.util;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

public class Snippet {
	 public static void  main(String args[]) throws IOException {
	       
	        System.out.println("************Teste de leitura do arquivo de propriedades************");
	         
	        Properties prop = AlgafoodUtil.getProp();
	         
	        String codBanco = prop.getProperty("boleto.bb.cod_banco");
	        String codMoeda = prop.getProperty("boleto.bb.cod_moeda");
	       // String codMoeda0 = prop.getProperty("boleto.bb.cod_moeda0");
	        String convenio = prop.getProperty("boleto.bb.covenio");
	        String complemento = prop.getProperty("boleto.bb.complemento");
	        String agencia = prop.getProperty("boleto.bb.cod_agencia");
	        String ccorrente = prop.getProperty("boleto.bb.conta_corre");
	        String carteira = prop.getProperty("boleto.bb.carteira");
	        String valor = "0000000100";
	        String fatVenci = AlgafoodUtil.getFatorVencimento(LocalDate.of(2020,4,15));
	         
	       // System.out.println("CodBanco = " + codBanco);
	       // System.out.println("Moeda = " + codMoeda);
	       // System.out.println("Fator vencimento = " + fatVenci);
	       //System.out.println("dias = "+AlgafoodUtil.getDias(LocalDate.of(2000,7,3)));
	       //System.out.println("dias = "+AlgafoodUtil.getDias(LocalDate.of(2020,5,14)));
	       // System.out.println("dias = "+AlgafoodUtil.getDias(LocalDate.of(2052,7,11)));
	       // System.out.println("dias = "+AlgafoodUtil.getDias(LocalDate.of(2079,11,27)));
	       String campo1 = codBanco+codMoeda+convenio;
	       campo1 = AlgafoodUtil.getDvDigitavel(campo1);
	       System.out.println(campo1);
	       String campo2 = AlgafoodUtil.getDvDigitavel(complemento+agencia);
	       String campo3 = AlgafoodUtil.getDvDigitavel(ccorrente+carteira);
	       
	       String s = campo1+" "+campo2+" "+campo3+" 3 "+fatVenci+valor+"\n";
	       											  
	       String cBarra = codBanco+codMoeda+fatVenci+valor+convenio+complemento+agencia+ccorrente+carteira;
	       String codigo= AlgafoodUtil.getDvBarra(cBarra, s);//44 -5º posição deve ser .
	       
	       String dv = codigo;
	       System.out.printf("vlr digitavel = "+s);
	       
	       System.out.printf("vlr barra = "+dv);
	       
	       String nossoNum = AlgafoodUtil.getNossoNumero("05009401448");
	       System.out.printf("\nNosso numero = "+nossoNum);
	    }
	 
}

//001982263 40144816069 06809350314 3 37370000000100