package net.kdt.pojavlaunch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.modmanager.ModManager;
import net.kdt.pojavlaunch.modmanager.State;
import net.kdt.pojavlaunch.modmanager.api.Modrinth;
import net.kdt.pojavlaunch.modmanager.api.ModData;

import java.util.ArrayList;

public class ModsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_mods_new, container, false);

        ModAdapter modAdapter = new ModAdapter(this);
        RecyclerView modRecycler = view.findViewById(R.id.mods_recycler);
        modRecycler.setLayoutManager(new LinearLayoutManager(modRecycler.getContext()));
        modRecycler.setAdapter(modAdapter);

        State.Instance selectedInstance = ModManager.state.getInstance("QuestCraft-1.18.2");
        Modrinth.addProjectsToRecycler(modAdapter, selectedInstance.getGameVersion(), 0, "");

        return view;
    }

    public static class ModViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ModsFragment fragment;
        private final ImageView icon;
        private final TextView title;
        private final TextView details;
        private final Switch enableSwitch;
        private ModData modData;

        public ModViewHolder(View view, ModsFragment fragment) {
            super(view);
            this.fragment = fragment;
            view.setOnClickListener(this);
            icon = view.findViewById(R.id.mod_icon);
            title = view.findViewById(R.id.mod_title);
            details = view.findViewById(R.id.mod_details);
            enableSwitch = view.findViewById(R.id.mod_switch);

            enableSwitch.setOnCheckedChangeListener((button, value) -> {
                State.Instance instance = ModManager.state.getInstance("QuestCraft-1.18.2");
                for (ModData modData : instance.getMods()) {
                    if (modData.slug.equals(this.modData.slug)) ModManager.setModActive("QuestCraft-1.18.2", this.modData.slug, value);
                    else ModManager.addMod("QuestCraft-1.18.2", this.modData.slug, "1.18.2");
                }
            });
        }

        public void setData(ModData modData) {
            this.modData = modData;
            title.setText(modData.title);
            details.setText(modData.title);

            if (!modData.iconUrl.isEmpty()) {
                Picasso.get().load(modData.iconUrl).into(icon);
            }
        }

        @Override
        public void onClick(View view) {
            View fView = fragment.getView();
            if (fView != null) {
                ImageView iconMain = fView.findViewById(R.id.mod_icon_main);
                TextView titleMain = fView.findViewById(R.id.mod_title_main);
                iconMain.setImageDrawable(icon.getDrawable());
                titleMain.setText(modData.title);
            }
        }
    }

    public static class ModAdapter extends RecyclerView.Adapter<ModViewHolder> {

        private final ModsFragment fragment;
        private final ArrayList<ModData> mods = new ArrayList<>();
        private int lastPosition = -1;

        public ModAdapter(ModsFragment fragment) {
            this.fragment = fragment;
        }

        public void addMods(ArrayList<ModData> newMods) {
            int startPos = mods.size();
            mods.addAll(newMods);
            this.notifyItemRangeChanged(startPos, mods.size());
        }

        @Override
        public int getItemViewType(final int position) {
            return R.layout.item_mod;
        }

        @NonNull
        @Override
        public ModViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ModViewHolder(view, fragment);
        }

        @Override
        public void onBindViewHolder(@NonNull ModViewHolder holder, int position) {
            if (mods.size() > position) {
                holder.setData(mods.get(position));
                setAnimation(holder.itemView, position);
            }
        }

        private void setAnimation(View viewToAnimate, int position) {
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(fragment.getContext(), android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

        @Override
        public int getItemCount() {
            return mods.size();
        }
    }
}
