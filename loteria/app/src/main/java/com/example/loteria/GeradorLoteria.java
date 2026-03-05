package com.example.loteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeradorLoteria {
    /*
        min = menor número permitido na loteria
        max = maior número permitido na loteria
        qtd = quantidade de números a serem sorteados
     */
    public static List<Integer> gerar(int min, int max, int qtd){

        //Lista que armazenará todos os números possíveis
        List<Integer> numeros = new ArrayList<>();

        //Preenche a lista com todos os números do intervalo passados para o metodo
        //Exemplo para Mega-Sena 1 até 60
        for(int i = min; i <= max; i++){
            numeros.add(i);
        }

        //Embaralhando aleatoriamente a ordem dos números
        Collections.shuffle(numeros);

        /*
            Pega apenas a quantidade de números solicitada, pelo índice da lista original
            Exemplo: se pediu 6 números, pega os 6 primeiros da lista embaralhada.
            Como? -> subList() retorna uma parte da lista baseada nos índices inicial e final, onde:
            o índice inicial é incluído e o índice final não é incluído
        */
        List<Integer> jogo = numeros.subList(0, qtd);

        //Ordena os números em ordem crescente
        Collections.sort(jogo);

        return jogo;
    }

}
