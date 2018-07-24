package com.liconic.table.exporttasks;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExportTaskTableModel {

    private final SimpleIntegerProperty idJob;
    private final SimpleStringProperty jobId;
    private final SimpleIntegerProperty idTask;
    private final SimpleStringProperty task;
    private final SimpleStringProperty status;
    private final SimpleStringProperty note;
    private final SimpleIntegerProperty number;

    public ExportTaskTableModel(int idJob, String JobId, int idTask, String task, String status, String note, int number) {

        this.idJob = new SimpleIntegerProperty(idJob);

        this.jobId = new SimpleStringProperty(JobId);

        this.idTask = new SimpleIntegerProperty(idTask);

        this.task = new SimpleStringProperty(task);
        this.status = new SimpleStringProperty(status);
        this.note = new SimpleStringProperty(note);

        this.number = new SimpleIntegerProperty(number);

    }

    public int getIdJob() {

        return idJob.get();
    }

    public void setIdJob(int idJob) {

        this.idJob.set(idJob);
    }

    public String getJobId() {

        return jobId.get();
    }

    public void setJobId(String JobId) {

        this.jobId.set(JobId);
    }

    public int getIdTask() {

        return idTask.get();
    }

    public void setIdTask(int idTask) {

        this.idTask.set(idTask);
    }

    public String getTask() {

        return task.get();
    }

    public void setTask(String task) {

        this.task.set(task);
    }

    public String getStatus() {

        return status.get();

    }

    public void setStatus(String status) {

        this.status.set(status);

    }

    public String getNote() {

        return note.get();

    }

    public void setNote(String note) {

        this.note.set(note);

    }

    public int getNumber() {

        return number.get();
    }

    public void setNumber(int number) {

        this.number.set(number);
    }

}
