package com.warungmakan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH> {

    interface Listener { void onChange(); }

    private final List<MenuItem> list;
    private final Listener listener;

    public MenuAdapter(List<MenuItem> list, Listener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        MenuItem item = list.get(pos);
        h.tvNama.setText(item.nama);
        h.tvHarga.setText("Rp " + formatRp(item.harga));
        h.tvJumlah.setText(String.valueOf(item.jumlah));

        h.btnPlus.setOnClickListener(v -> {
            item.jumlah++;
            h.tvJumlah.setText(String.valueOf(item.jumlah));
            listener.onChange();
        });

        h.btnMinus.setOnClickListener(v -> {
            if (item.jumlah > 0) {
                item.jumlah--;
                h.tvJumlah.setText(String.valueOf(item.jumlah));
                listener.onChange();
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

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

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga, tvJumlah;
        Button btnPlus, btnMinus;

        VH(View v) {
            super(v);
            tvNama = v.findViewById(R.id.tvNama);
            tvHarga = v.findViewById(R.id.tvHarga);
            tvJumlah = v.findViewById(R.id.tvJumlah);
            btnPlus = v.findViewById(R.id.btnPlus);
            btnMinus = v.findViewById(R.id.btnMinus);
        }
    }
}
