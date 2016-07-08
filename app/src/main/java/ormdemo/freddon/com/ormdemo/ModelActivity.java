package ormdemo.freddon.com.ormdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by fred on 16/7/8.
 */
public class ModelActivity extends BaseActivity {

    private static final String INTENT_DATA_MODEL = "INTENT_DATA_MODEL";
    private View buttonMod;
    private EditText etId;
    private EditText etName;
    private Model model;

    public static Intent newIntent(Context context, Model model) {
        Intent intent = new Intent();
        intent.setClass(context, ModelActivity.class);
        intent.putExtra(INTENT_DATA_MODEL, model);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        if (getIntent().hasExtra(INTENT_DATA_MODEL)) {
            model = getIntent().getParcelableExtra(INTENT_DATA_MODEL);
            if (model == null) {
                finish();
                return;
            }
            initViewsAndListeners();
        } else {
            finish();
        }

    }

    private void initViewsAndListeners() {
        buttonMod = findViewById(R.id.buttonMod);
        etId = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        etId.setText(String.valueOf(model.getId()));
        etName.setText(model.getName());


        buttonMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etId.getText())) {
                    showCustomToast("", "不能为空");
                    return;
                }

                model.setId(Integer.parseInt(etId.getText().toString()));
                model.setName(etName.getText().toString());
                modRecord(model);
            }
        });

    }

    /**
     * 修改
     */
    private void modRecord(Model item) {
        if (item != null && item.get_id() != 0) {
            try {
                Dao.CreateOrUpdateStatus status = ModelHelper.getHelper(this).getModelDao().createOrUpdate(item);//传入id则更新
                if (status.isCreated() || status.isUpdated()) {
                    showCustomToast("提示", String.format("数据 => %s 【%s】成功", String.valueOf(item), status.isCreated() ? "插入" : "更新"));
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showCustomToast("提示", "数据操作失败");
                }
            } catch (SQLException e) {
                showCustomToast("提示", "数据查询失败 " + e.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
