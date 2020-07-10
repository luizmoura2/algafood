package com.algaworks.algafood.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import org.springframework.util.ReflectionUtils;

public class AlgafoodUtil {
	
	public <T> T copyx(final T source, final T target) {
		
	    ReflectionUtils.doWithFields(target.getClass(), new ReflectionUtils.FieldCallback() {
		    @Override
		    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
		      field.setAccessible(true);
		      if (field.get(source) != null){		    	
		    	  field.set(target, field.get(source));
		      }
		    }
	  }, ReflectionUtils.COPYABLE_FIELDS);
	 
	    return target;
	}
	
	
	public static Properties getProp() throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream("src/main/resources/dados.properties");
        props.load(file);
        return props; 
    }
	
	
	public static String getFatorVencimento(LocalDate vencimento) {
		
		//define datas
		LocalDate dataCadastro = LocalDate.of(1997, 10, 7);
		
		long dias = dataCadastro.until(vencimento, ChronoUnit.DAYS);
		
		if (dias > 9999 && dias < 10000) {
			 dias = dias-9000;
		}
		if (dias >= 10000 && dias < 20000) {
			 dias = dias-9000;
		}
		if (dias >= 20000 && dias < 30000) {
			 dias = dias-19000;
		}
		if (dias >= 30000 && dias < 40000) {
			 dias = dias-29000;
		}
		return String.valueOf(dias);
	}
	
	public static String getDvDigitavel(String campo) {
		int y=0;
		int x=0;
		int z=0;
		String vlr = campo.replace(".", "");
		int n = vlr.length();
		
		for(int i=0; i<n; i++){//9
			x = Character.getNumericValue(campo.charAt(i));
			y = (int)x;
			
			if (n%2 > 0) {
				if (i%2==0) {				
					y = (int) x*2;
				}
			}else if (i%2 > 0) {
				y = (int) x*2;
			}
						
			if (y>10) {
				y= y-10+1;
			}
			z=z+y;
			
		}
		z=10-z%10;
		
		return String.valueOf(campo+z);
	}
	
	public static String getDvBarra(String codigo, String s) {
		int y=0;
		int x=0;
		int i=2;
		String vlr = codigo.replace(".", "");
		int n = vlr.length();
		for(int j=n-1; j>=0; j--) {
				x = Character.getNumericValue(vlr.charAt(j));
				x = x*i;
				if (i==9) {
					i=1;
				}
				y = y+x;
				i++;
		}
		y = 11-y%11;
		if (y == 0 || y == 10 || y == 11) y=1;
		StringBuffer sd = new StringBuffer(s);
		 sd.insert(5, ".");
		 sd.insert(17, ".");
		 sd.insert(30, ".");
		 sd.setCharAt(38,  Character.forDigit(y, 10));
		 
		StringBuffer sb = new StringBuffer(codigo);
		 sb.insert(4, Character.forDigit(y, 10));
		//setCharAt(4,  Character.forDigit(y, 10));
		 //sb.delete(9, 10);		 
		 //sb.delete(19, 20);
		 //sb.delete(29, 30);
		return sb.toString()+"\n"+sd.toString();
	}
	
	public static String getNossoNumero(String codigo){
		int y=0;
		int x=0;
		int i=9;
		int n = codigo.length();
		for(int j=n-1; j>=0; j--) {
			x = Character.getNumericValue(codigo.charAt(j));
			
			x = x*i;
			if (i==2) {
				i=10;
			}
			y = y+x;
			i--;
		}
		
		y = y%11;
		if (y == 11) { 
			codigo = codigo+"-x";
		}
		codigo = codigo+"-"+String.valueOf(y);
		
		return codigo;
		
	}
}
