package lekwaito.oroni.lekwaito;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import lekwaito.oroni.lekwaito.Adapters.ProductListAdapter;
import lekwaito.oroni.lekwaito.Model.Product;

public class ProductClothing extends Activity {

    SQLiteHelper sqLiteHelper;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        sqLiteHelper = new SQLiteHelper(this);

        list = (ListView) findViewById(R.id.clothing_list);
        ArrayList<Product> prods = sqLiteHelper.getAllProductData(getIntent().getStringExtra("type"));
        Product[] prodArray = new Product[prods.size()];
        for(int i = 0 ; i<prodArray.length;i++)
            prodArray[i] = prods.get(i);
        ProductListAdapter ListAdpater =  new ProductListAdapter(this, R.layout.product_list_item,prodArray,
                                                                            getIntent().getStringExtra("emailholder"));
        list.setAdapter(ListAdpater);

    }
}
