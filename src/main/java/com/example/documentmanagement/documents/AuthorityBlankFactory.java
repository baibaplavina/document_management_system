package com.example.documentmanagement.documents;

import com.example.documentmanagement.insolvencyProcess.InsolvencyProcess;

public class AuthorityBlankFactory {

    public AuthorityBlank getAuthorityBlank(InsolvencyProcess insolvencyProcess, int number){

        if(number == 1) {
            return new AuthorityBlank_1(insolvencyProcess);
        }
        else if(number == 2){
            return new AuthorityBlank_2(insolvencyProcess);
        }
        else if(number == 3) {
            return new AuthorityBlank_3(insolvencyProcess);
        }
        else if(number == 4){
            return new AuthorityBlank_4(insolvencyProcess);
        }
        else if(number == 5) {
            return new AuthorityBlank_5(insolvencyProcess);
        }
        else if(number == 6) {
            return new AuthorityBlank_6(insolvencyProcess);
        }
        return null;
    }
}
