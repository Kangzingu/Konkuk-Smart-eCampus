package kksy.konkuk_smart_ecampus;

public class TimeLineBoardFormat {
    private String boardID;
    private boolean wantTop;//상단바 고정 여부, true일 경우 날짜보다 우선 순위가 높음, 초기값:false
    private boolean isread;//읽었니? true일 경우, 추후에 비활성화 시켜서 표시 해야됨, 초기값:false

    public boolean isWantTop() {
        return wantTop;
    }

    public boolean isIsread() {
        return isread;
    }

    public TimeLineBoardFormat(){

    }
    public TimeLineBoardFormat(String boardID) {
        this.boardID = boardID;
        this.wantTop = false;
        this.isread=false;
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }



    public void setWantTop(boolean wantTop) {
        this.wantTop = wantTop;
    }



    public void setIsread(boolean isread) {
        this.isread = isread;
    }
}
