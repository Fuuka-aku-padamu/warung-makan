package com.warungmakan;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import androidx.core.splashscreen.SplashScreen;


public class MainActivity extends AppCompatActivity {
    private List<MenuItem> menuList;
    private TextView tvTotal;
    private Button btnBayar;
    private int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotal = findViewById(R.id.tvTotal);
        btnBayar = findViewById(R.id.btnBayar);

        menuList = new ArrayList<>();

        menuList.add(new MenuItem("Nasi Goreng Ngawi", 25000));
        menuList.add(new MenuItem("Mie Ayam", 14000));
        menuList.add(new MenuItem("Soto Ayam", 14000));
        menuList.add(new MenuItem("Ayam Bakar", 25000));
        menuList.add(new MenuItem("Gado-Gado", 15000));
        menuList.add(new MenuItem("Rawon", 25000));
        menuList.add(new MenuItem("Rendang Sapi", 35000));
        menuList.add(new MenuItem("Udang Goreng", 32000));
        menuList.add(new MenuItem("Es Teh Manis Solo", 5000));
        menuList.add(new MenuItem("Juk Jerus Peras", 10000));
        menuList.add(new MenuItem("Es Krim Berlapis Emas", 125000));

        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new MenuAdapter(menuList, this::hitungTotal));

        btnBayar.setOnClickListener(v -> showDialog());
    }

    private void hitungTotal() {
        total = 0;
        for (MenuItem m : menuList) total += m.harga * m.jumlah;
        tvTotal.setText("Total: Rp " + formatRp(total));
        btnBayar.setEnabled(total > 0);
    }

    private void showDialog() {
        String[] pilihan = {"Tanpa Diskon", "Diskon Member 10%", "Diskon Malam Hari 5%"};
        final int[] pilih = {0};

        new AlertDialog.Builder(this)
            .setTitle("Pilih Diskon")
            .setSingleChoiceItems(pilihan, 0, (d, which) -> pilih[0] = which)
            .setPositiveButton("Bayar", (d, w) -> {
                double persen = pilih[0] == 1 ? 0.10 : pilih[0] == 2 ? 0.05 : 0;
                int potongan = (int)(total * persen);
                int bayar = total - potongan;

                String pesan = "Subtotal : Rp " + formatRp(total) + "\n"
                    + "Diskon   : Rp " + formatRp(potongan) + "\n"
                    + "Total Bayar: Rp " + formatRp(bayar);

                new AlertDialog.Builder(this)
                    .setTitle("Pembayaran Berhasil!")
                    .setMessage(pesan)
                    .setPositiveButton("Selesai", (d2, w2) -> resetPesanan())
                    .show();
            })
            .setNegativeButton("Batal", null)
            .show();
    }

    private void resetPesanan() {
        for (MenuItem m : menuList) m.jumlah = 0;
        total = 0;
        tvTotal.setText("Total: Rp 0");
        btnBayar.setEnabled(false);
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.getAdapter().notifyDataSetChanged();
    }

    private String formatRp(int n) {
        String s = String.valueOf(n);
        StringBuilder r = new StringBuilder();
        int c = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            r.insert(0, s.charAt(i));
            if (++c % 3 == 0 && i != 0) r.insert(0, '.');
        }
        return r.toString();
    }
}
