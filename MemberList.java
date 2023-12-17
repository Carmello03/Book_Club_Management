
/**
 * Write a description of class MemberList here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MemberList extends ObjectList {

    public MemberList(int capacity) {
        super(capacity);
    }

    public boolean add(Member member) throws ClubFullException {
        return super.add(member); 
    }

    public Member search(int index) {
        return (Member)super.getObject(index);
    }

    public boolean remove(int index) {
        return super.remove(index);
    }

}
