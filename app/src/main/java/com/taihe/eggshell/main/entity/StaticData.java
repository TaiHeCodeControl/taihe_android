package com.taihe.eggshell.main.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.chinaway.framework.swordfish.db.annotation.Id;
import com.chinaway.framework.swordfish.db.annotation.NoAutoIncrement;
import com.chinaway.framework.swordfish.db.annotation.Table;

/**
 * Created by wang on 2015/9/2.
 */
@Table(name = "table_static")
public class StaticData implements Parcelable{

    @Id(column = "id")
    @NoAutoIncrement
    private int id;
    private String name;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }

    public static final Creator<StaticData> CREATOR = new Creator<StaticData>() {
        @Override
        public StaticData createFromParcel(Parcel source) {
            StaticData data = new StaticData();
            data.id = source.readInt();
            data.name = source.readString();
            return data;
        }

        @Override
        public StaticData[] newArray(int size) {
            return new StaticData[size];
        }
    };
}
