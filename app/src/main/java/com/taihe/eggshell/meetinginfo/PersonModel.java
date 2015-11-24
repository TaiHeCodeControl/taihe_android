package com.taihe.eggshell.meetinginfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wang on 2015/11/24.
 */
public class PersonModel implements Parcelable{

    private String id;
    private String studentsname;
    private String studentsnature;
    private String studentsphoto;
    private String cityname;
    private String content;
    private String motto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentsname() {
        return studentsname;
    }

    public void setStudentsname(String studentsname) {
        this.studentsname = studentsname;
    }

    public String getStudentsnature() {
        return studentsnature;
    }

    public void setStudentsnature(String studentsnature) {
        this.studentsnature = studentsnature;
    }

    public String getStudentsphoto() {
        return studentsphoto;
    }

    public void setStudentsphoto(String studentsphoto) {
        this.studentsphoto = studentsphoto;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(studentsname);
        parcel.writeString(studentsnature);
        parcel.writeString(studentsphoto);
        parcel.writeString(cityname);
        parcel.writeString(motto);
        parcel.writeString(content);

    }

    public static final Creator<PersonModel> CREATOR = new Creator<PersonModel>() {
        @Override
        public PersonModel createFromParcel(Parcel parcel) {

            PersonModel personModel = new PersonModel();
            personModel.id = parcel.readString();
            personModel.studentsname = parcel.readString();
            personModel.studentsnature = parcel.readString();
            personModel.studentsphoto = parcel.readString();
            personModel.cityname = parcel.readString();
            personModel.motto = parcel.readString();
            personModel.content = parcel.readString();

            return personModel;
        }

        @Override
        public PersonModel[] newArray(int i) {
            return new PersonModel[i];
        }
    };
}
