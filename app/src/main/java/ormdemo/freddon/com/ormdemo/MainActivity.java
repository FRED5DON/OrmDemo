package ormdemo.freddon.com.ormdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_MODEL = 1;
    private ModelAdapter adapter;
    private View buttonAdd;
    private EditText etId;
    private EditText etName;
    private ListView mListView;
    private List<Model> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewsAndListeners();
        queryRecord();
    }

    private void initViewsAndListeners() {
        buttonAdd = findViewById(R.id.buttonAdd);
        etId = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        buttonAdd.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.listView);
        data = new ArrayList<>();
        adapter = new ModelAdapter(this, data);
        mListView.setAdapter(adapter);
        adapter.setModelClickListener(new ModelAdapter.ModelClick() {
            @Override
            public void onLongClick(Object tag) {
                if (tag instanceof Model) {
                    Model model = (Model) tag;
                    delRecord(model.get_id());
                    queryRecord();
                }

            }

            @Override
            public void onClick(Object tag) {
                if (tag instanceof Model) {
                    Model model = (Model) tag;
                    startActivityForResult(ModelActivity.newIntent(getApplicationContext(), model), REQUEST_MODEL);
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MODEL) {
            if (resultCode==RESULT_OK){
                queryRecord();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                Model model = new Model();
                model.setId(Integer.parseInt(etId.getText().toString()));
                model.setName(etName.getText().toString());
                addRecord(model);
                queryRecord();
                break;
            default:
                break;
        }
    }

    /**
     * 查询
     */
    private void queryRecord() {
        try {
            List<Model> list = ModelHelper.getHelper(this).getModelDao().queryForAll();
            adapter.notifyData(list);
            showCustomToast("提示", String.format("数据查询结果 => %s ", String.valueOf(list)));
        } catch (SQLException e) {
            showCustomToast("提示", "数据查询失败 " + e.getMessage());
        }
    }


    /**
     * 删除
     *
     * @param _id 一定要是自增_id 或者自己重新写个方法不按_id删除
     */
    private void delRecord(long _id) {
        try {
            int status = ModelHelper.getHelper(this).getModelDao()
                    .delete(ModelHelper.getHelper(this).getModelDao().queryForId(_id));
            showCustomToast("提示", String.format(" %d rows are affected ,删除了_id=%d的记录", status, _id));
        } catch (SQLException e) {
            showCustomToast("提示", "数据表清空失败 " + e.getMessage());
        }
    }

    /**
     * 增加一条记录
     *
     * @param model
     */
    private void addRecord(Model model) {
        try {
            Dao.CreateOrUpdateStatus status = ModelHelper.getHelper(this).getModelDao().createOrUpdate(model);//传入id则更新
            if (status.isCreated() || status.isUpdated()) {
                showCustomToast("提示", String.format("数据 => %s 【%s】成功", String.valueOf(model), status.isCreated() ? "插入" : "更新"));
            } else {
                showCustomToast("提示", "数据操作失败");
            }
        } catch (SQLException e) {
            showCustomToast("提示", "数据查询失败 " + e.getMessage());
        }
    }


}
