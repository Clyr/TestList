package com.matrix.myapplication.view.treelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matrix.myapplication.R;
import com.matrix.myapplication.model.Model;

import java.util.List;

public class TreeListView2 extends ListView {
	ListView treelist = null;
	TreeAdapter2 mAdapter2 = null;

	@SuppressLint("WrongConstant")
	public TreeListView2(Context context, List<Model> res) {
		super(context);
		treelist = this;
		treelist.setFocusable(false);
		treelist.setFadingEdgeLength(0);
		treelist.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		treelist.setDrawSelectorOnTop(false);
		treelist.setCacheColorHint(0xffffff);
		treelist.setDivider(getResources().getDrawable(R.drawable.divider_list));
		treelist.setDividerHeight(2);
		treelist.setFastScrollEnabled(true);
		treelist.setScrollBarStyle(NO_ID);
		treelist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("print", "被点击"+position);

			}
		});
		mAdapter2 = new TreeAdapter2(context, res);
		treelist.setAdapter(mAdapter2);
	}

	public void initNode(Context context, List<Node> root, boolean hasCheckBox,
			int tree_ex_id, int tree_ec_id, int expandLevel) {
//		ta = new TreeAdapter2(context, root);
//		int tree_ex_id_ = (tree_ex_id == -1) ? R.drawable.tree_ex : tree_ex_id;
//		int tree_ec_id_ = (tree_ec_id == -1) ? R.drawable.tree_ec : tree_ec_id;
//		ta.setCollapseAndExpandIcon(tree_ex_id_, tree_ec_id_);
//		this.setAdapter(ta);
	}

	/* 返回当前所有选中节点的List数组 */
//	public List<Node> get() {
//		return ta.getSelectedNode();
//	}

}
