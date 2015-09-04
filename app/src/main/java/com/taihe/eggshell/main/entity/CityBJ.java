package com.taihe.eggshell.main.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.chinaway.framework.swordfish.db.annotation.Id;
import com.chinaway.framework.swordfish.db.annotation.NoAutoIncrement;
import com.chinaway.framework.swordfish.db.annotation.Table;

/**
 * Created by wang on 2015/9/4.
 */
@Table(name = "table_city")
public class CityBJ implements Parcelable{

    @Id(column = "id")
    @NoAutoIncrement
    private int id;
    private int keyid;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKeyid() {
        return keyid;
    }

    public void setKeyid(int keyid) {
        this.keyid = keyid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeInt(keyid);
        parcel.writeString(name);
    }

    public static final Creator<CityBJ> CREATOR = new Creator<CityBJ>() {
        @Override
        public CityBJ createFromParcel(Parcel source) {
            CityBJ city = new CityBJ();
            city.id = source.readInt();
            city.keyid = source.readInt();
            city.name = source.readString();
            return city;
        }

        @Override
        public CityBJ[] newArray(int size) {
            return new CityBJ[size];
        }
    };
}
