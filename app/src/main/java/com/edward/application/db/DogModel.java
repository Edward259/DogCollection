package com.edward.application.db;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;
import java.util.Objects;

@Table(database = DogDatabase.class)
public class DogModel extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    private int id;
    @Column
    private String breed;
    @Column
    private String coverPath;
    @Column(typeConverter = ConverterStringList.class)
    private List pics;
    @Column
    private boolean isCollected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getPics() {
        return pics;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()
                || TextUtils.isEmpty(getBreed())
                || TextUtils.isEmpty(((DogModel) o).getBreed())) {
            return false;
        }
        DogModel dogModel = (DogModel) o;
        return dogModel.getBreed().equals(((DogModel) o).getBreed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, breed, coverPath, pics);
    }

    @Override
    public String toString() {
        return "DogModel{" +
                "id=" + id +
                ", breed='" + breed + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", pics=" + pics +
                '}';
    }
}
