import java.util.ArrayList;
class StudyRoomReservationSystem{
    static ArrayList<StudyRoom> studyRoomsList; 

    // main method
    public static void main(String[] args) throws InterruptedException  {
        studyRoomsList = new ArrayList<StudyRoom>();
        studyRoomsList.add(new StudyRoom(1, 4, true));
        studyRoomsList.add(new StudyRoom(2, 6, true));
        studyRoomsList.add(new StudyRoom(3, 8, true));

        //status of all study rooms at the beginning
        displayStudyRoomStatus();
        
        // first child thead
        Thread child1 = new Thread(new Runnable(){
            public void run(){
                try{
                    reserveStudyRoom(1);
                    
                }catch(StudyRoomUnavailableException e){
                    System.out.println(e.getMessage());
                }
            }
        });

        // second child thead
        Thread child2 = new Thread(new Runnable(){
            public void run(){
                try{
                    
                    reserveStudyRoom(2);

                }catch(StudyRoomUnavailableException e){
                    System.out.println(e.getMessage());
                }
            }
        });

        // third child thead
        Thread child3 = new Thread(new Runnable(){
            public void run(){
               releaseStudyRoom(1);
            }
        });

        child1.start();
        child2.start();
        child3.start();

    
        child1.join();
        child2.join();
        child3.join();
        

        //status of all study rooms at the end
        displayStudyRoomStatus();

        


        
    }

    // add a Room
    public static void addStudyRoom(StudyRoom studyRoom){ 
        if(studyRoomsList == null){
            studyRoomsList = new ArrayList<StudyRoom>();
        }
        studyRoomsList.add(studyRoom);
    }

    // reserve a Rooom
    public static void reserveStudyRoom(int roomNumber) throws StudyRoomUnavailableException{ 
        for(StudyRoom studyRoom : studyRoomsList){
            if(studyRoom.getNumberOfRoom() == roomNumber){
                // synchronize statement
                synchronized(studyRoom){
                    // check whether the room is alredy available
                    if(studyRoom.getIsAvailable() == true){
                        studyRoom.setIsAvailable(false);      
                    }else{
                        throw new StudyRoomUnavailableException("Error : Study room " + roomNumber + " is not available.");  
                    }
                }

            }
        }
    }

    // release the Rooom
    public static synchronized void releaseStudyRoom(int roomNumber){ 
        for(StudyRoom studyRoom : studyRoomsList){
            if(studyRoom.getNumberOfRoom() == roomNumber){
                //synchronize statement
                synchronized(studyRoom){
                    // check whether the room is alredy available
                    if(studyRoom.getIsAvailable() == false){
                        studyRoom.setIsAvailable(true);      
                    }
                }
                
            }
        }
    }

    //display the current Room Stats
    public  static void displayStudyRoomStatus(){ 
        System.out.println("Study Room Status:");
        for(StudyRoom studyRoom : studyRoomsList){
            System.out.println("Room Number: " + studyRoom.getNumberOfRoom() + ", Capacity: " +studyRoom.getNumberOfSeats()+ ", Availability: "+ (studyRoom.getIsAvailable() ? "Available" : "Unavailable"));
        }
        
        
    }


}
class StudyRoom{
    private int numberOfRoom;
    private int numberOfSeats;
    private boolean isAvailable;

    public StudyRoom(int numberOfRoom, int numberOfSeats, boolean isAvailable){
        this.numberOfRoom = numberOfRoom;
        this.numberOfSeats = numberOfSeats;
        this.isAvailable = isAvailable;
    }

    public int getNumberOfRoom(){
        return numberOfRoom;
    }

    public int getNumberOfSeats(){
        return numberOfSeats;
    }

    public boolean getIsAvailable(){
        return isAvailable;
    }

    public void setNumberOfRoom(int numberOfRoom){
        this.numberOfRoom = numberOfRoom;
    }

    public void setNumberOfSeats(int numberOfSeats){
        this.numberOfSeats = numberOfSeats;
    }

    public void setIsAvailable(boolean isAvailable){
        this.isAvailable = isAvailable;
    }

    

}

class StudyRoomUnavailableException extends Exception {
    public StudyRoomUnavailableException(String message) {
        super(message);
    }
    
}
