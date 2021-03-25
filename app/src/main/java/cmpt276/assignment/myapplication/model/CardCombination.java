/**
  Card Logic is responsible for choosing the correct set of images for the game play.
 */
package cmpt276.assignment.myapplication.model;

import java.util.ArrayList;
import java.util.List;

public class CardCombination {
    private List<int []> cards= new ArrayList<>();
    //holds combination of cards
    private int[] CombinationOfDraw; //
    private int ImagesPerCard;
    private int DeckSize;

    private int Draw1=-100;
    private int Draw2=-101;
    private int Draw3=-102;
    private int Draw4=-103;
    private int Draw5=-104;
    private int Draw6=-105;

    private int Discard1=-106;
    private int Discard2=-107;
    private int Discard3=-108;
    private int Discard4=-109;
    private int Discard5=-110;
    private int Discard6=-111;
    public static void setInstance(CardCombination instance) {
        CardCombination.instance = instance;
    }
    public void refresh(){
        instance = null;
    }

    private static CardCombination instance;
    public static CardCombination getInstance(){
        if(instance==null){
            instance= new CardCombination();
        }
        return instance;
    }

    private CardCombination(){

    }

    public int[] getCombinationOfDraw() {
        return CombinationOfDraw;
    }

    public void setCombinationOfDraw(int[] combinationOfDraw) {
        CombinationOfDraw = combinationOfDraw;
    }

    public void removeObject(int [] arr)
    {
        cards.remove(arr);
    }

    public void remove(int i){
        cards.remove(i);
    }
    public int size(){
        return cards.size();
    }

    public int []get(int i){
        return cards.get(i);
    }

    public int getDiscard1() {
        return Discard1;
    }

    public void setDiscard1(int discard1) {
        Discard1 = discard1;
    }

    public int getDiscard2() {
        return Discard2;
    }

    public void setDiscard2(int discard2) {
        Discard2 = discard2;
    }

    public int getDiscard3() {
        return Discard3;
    }

    public void setDiscard3(int discard3) {
        Discard3 = discard3;
    }

    public void setCards(List<int[]> cards) {
        this.cards = cards;
    }
    public List<int[]> getCards()
    {
        return cards;
    }

    public int getImagesPerCard() {
        return ImagesPerCard;
    }

    public void setImagesPerCard(int imagesPerCard) {
        ImagesPerCard = imagesPerCard;
    }

    public int getDeckSize() {
        return DeckSize;
    }

    public void setDeckSize(int deckSize) {
        DeckSize = deckSize;
    }

    public int getDiscard4() {
        return Discard4;
    }

    public void setDiscard4(int discard4) {
        Discard4 = discard4;
    }

    public int getDiscard5() {
        return Discard5;
    }

    public void setDiscard5(int discard5) {
        Discard5 = discard5;
    }

    public int getDiscard6() {
        return Discard6;
    }

    public void setDiscard6(int discard6) {
        Discard6 = discard6;
    }

    public int getDraw1() {
        return Draw1;
    }

    public void setDraw1(int draw1) {
        Draw1 = draw1;
    }

    public int getDraw2() {
        return Draw2;
    }

    public void setDraw2(int draw2) {
        Draw2 = draw2;
    }

    public int getDraw3() {
        return Draw3;
    }

    public void setDraw3(int draw3) {
        Draw3 = draw3;
    }

    public int getDraw4() {
        return Draw4;
    }

    public void setDraw4(int draw4) {
        Draw4 = draw4;
    }

    public int getDraw5() {
        return Draw5;
    }

    public void setDraw5(int draw5) {
        Draw5 = draw5;
    }

    public int getDraw6() {
        return Draw6;
    }

    public void setDraw6(int draw6) {
        Draw6 = draw6;
    }
}
