package com.example.tcc;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tcc.clientes.ClientesFragment;
import com.example.tcc.produtos.ProdutosFragment;
import com.example.tcc.vendas.VendasFragment;

//ADAPTADOR DO FRAGMENTO PARA O VIEWPAGER
public class VPAdapater extends FragmentStateAdapter{

    public VPAdapater(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    //QUANDO CRIADO VAI CRIAR COM CADA FRAGMENTO RESPECTIVA TELA NA RESPECTIVA POSIÇÃO
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new FragInicio();
                break;
            case 1:
                fragment = new ProdutosFragment();
                break;
            case 2:
                fragment = new VendasFragment();
                break;
            case 3:
                fragment = new ClientesFragment();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;
    }

    //TOTAL DE FRAGMENTOS FIXO EM 4 (INICIO,PRODUTOS,VENDAS,CLIENTES)
    @Override
    public int getItemCount() {
        return 4;
    }
}
