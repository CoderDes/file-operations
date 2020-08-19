package com.eugz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LimitBook {
    private List<Order> orders;
    private final String QUERY_BID = "best_bid";
    private final String QUERY_ASK = "best_ask";
    private final String QUERY_SIZE = "size";
    private final String ORDER_BUY = "buy";
    private final String ORDER_SELL = "sell";
    private String queryResult = "";


    public LimitBook() {
        this.orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public String getQueryResult() {
        return queryResult;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void removeOrder(int targetOrderID) {
        Iterator iter = orders.iterator();

        while (iter.hasNext()) {
            Order order = (Order) iter.next();
            if (targetOrderID == order.getOrderID()) {
                if (order.getSize() > 1) {
                    order.decreaseSize();
                    return;
                }
                iter.remove();
            }
        }
    }

    public void query(String queryString) {
        if (queryString.equals(QUERY_BID)) {
            Order theLowestAsk = findLowestAsk(getOrdersByType("ask"));
            int theLowestAskPrice = theLowestAsk.getPrice();
            Order theBestBid = findTheBestBid(theLowestAskPrice);
            addToQueryResult(theBestBid.getPrice() + "," + theBestBid.getSize());
        } else if (queryString.equals(QUERY_ASK)) {
            Order theBestAsk = findLowestAsk(getOrdersByType("ask"));
            addToQueryResult(theBestAsk.getPrice() + "," + theBestAsk.getSize());
        }
    }

    private void addToQueryResult(String s) {
        this.queryResult = this.queryResult.concat(s + "\n");
    }

    public void query(String queryString, int price) {
        if (!queryString.equals(QUERY_SIZE)) {
            return;
        }
        for (Order order : orders) {
            int currentPrice = order.getPrice();
            if (currentPrice == price) {
                addToQueryResult(Integer.toString(order.getSize()));
            }
        }
    }

    private Order findLowestAsk(List<Order> asks) {
        Iterator askIter = asks.iterator();
        Order theSmallest = asks.get(0);
        while(askIter.hasNext()) {
            Order ask = (Order) askIter.next();
            if (ask.getPrice() < theSmallest.getPrice()) {
                theSmallest = ask;
            }
        }

        return theSmallest;
    }

    private Order findTheBestBid(int priceThreshold) {
        List<Order> bids = getOrdersByType("bid");

        Order theBestBid = bids.get(0);
        for (Order bid : bids) {
            int currentBidPrice = bid.getPrice();
            if (currentBidPrice < priceThreshold && currentBidPrice >= theBestBid.getPrice()) {
                theBestBid = bid;
            }
        }

        return theBestBid;
    }

    private List<Order> getOrdersByType(String type) {
        List <Order> orders = new ArrayList<>();

        Iterator iter = this.orders.iterator();
        while (iter.hasNext()) {
            Order order = (Order) iter.next();
            if (order.getOrderType().equals(type)) {
                orders.add(order);
            }
        }

        return orders;
    }

    public void order(String operation, int size) {
        if (operation.equals(ORDER_BUY)) {
            List<Order> asksBySize = getOrdersBySize("ask", size);
            Order cheapestAsk = findLowestAsk(asksBySize);
            removeOrder(cheapestAsk.getOrderID());
        } else if (operation.equals(ORDER_SELL)){
            List<Order> allBids = getOrdersByType("bid");
            Order mostExpensiveBid = findTheMostExpensiveBid(allBids);
            removeOrder(mostExpensiveBid.getOrderID());
        }
    }

    private List<Order> getOrdersBySize(String orderType, int size) {
        List<Order> allOrders = getOrdersByType(orderType);
        List<Order> filteredOrders = new ArrayList<>();

        Iterator iter = allOrders.iterator();
        while(iter.hasNext()) {
            Order order = (Order) iter.next();
            if (order.getSize() == size) {
                filteredOrders.add(order);
            }
        }

        return filteredOrders;
    }

    private Order findTheMostExpensiveBid(List<Order> bids) {
        Order theMostExpensive = bids.get(0);
        for (Order bid : bids) {
            if (bid.getPrice() >= theMostExpensive.getPrice()) {
                theMostExpensive = bid;
            }
        }

        return theMostExpensive;
    }
}
