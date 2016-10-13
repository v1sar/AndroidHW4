package ru.mail.park.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.kohsuke.randname.RandomNameGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private static final RandomNameGenerator GENERATOR = new RandomNameGenerator();

    private RecyclerView recyclerView;

    private GridLayoutManager gLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DataSource dataSource = new DataSource();
        recyclerView = (RecyclerView) findViewById(R.id.container);

        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ItemViewHolder(
                        getLayoutInflater().inflate(R.layout.text, parent, false)
                );
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Item item = dataSource.getItem(position);
                ((ItemViewHolder) holder).bind(item);
            }

            @Override
            public int getItemCount() {
                return dataSource.getCount();
            }

        });

        gLayoutManager = new GridLayoutManager(this, 3);
        gLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(position % 7){
                    case 1:
                    case 5:
                    case 6:
                        return 3;
                    default:
                        return 1;
                }
            }
        });

        recyclerView.setLayoutManager(gLayoutManager);

        findViewById(R.id.add_long).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.addItem(generateItemLong());
            }
        });

        findViewById(R.id.add_short).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.addItem(generateItemShort());
            }
        });

        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.removeFirst();
            }
        });

        findViewById(R.id.grid2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setLayoutManager(gLayoutManager);
            }
        });

        findViewById(R.id.grid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,1));
            }
        });
    }

    private Item generateItemLong() {
        return new Item(GENERATOR.next()+" "+GENERATOR.next());
    }
    private Item generateItemShort() {
        return new Item("text");
    }

    private static class Item {

            private final String text1;
            private final String text2;
            private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

        Item(String text) {
            this.text1 = "Item: "+ ID_GENERATOR.getAndIncrement();;
            this.text2 = text;
        }

        public String getText1() {
            return text1;
        }

        public String getText2() {
            return text2;
        }
    }

    private class DataSource {

        private final List<Item> items = new ArrayList<>();

        public int getCount() {
            return items.size();
        }

        public Item getItem(int position) {
            return items.get(position);
        }

        public void addItem(Item item) {
            items.add(item);
            recyclerView.getAdapter().notifyItemInserted(items.size() - 1);
        }

        public void removeFirst() {
            if (!items.isEmpty()) {
                items.remove(0);
                recyclerView.getAdapter().notifyItemRemoved(0);
            }
        }

    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView text1;
        private final TextView text2;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.text1 = (TextView) itemView.findViewById(R.id.text1);
            this.text2 = (TextView) itemView.findViewById(R.id.text2);
        }

        public void bind(Item item) {
            text1.setText(item.getText1());
            text2.setText(item.getText2());
        }

    }

}
