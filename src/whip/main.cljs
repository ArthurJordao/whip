(ns ^:figwheel-always whip.main
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed from src/whip/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state
  (reagent/atom
    {:projects
     {"aaa"
      {:title "Build Whip"
       :stories
       {1 {:title "Design a data model for projects and stories"
           :status "done"
           :order 1}
        2 {:title "Create a story title entry form"
           :order 2}
        3 {:title "Implement a way to finish stories"
           :order 3}}}}}))


(defn story-card
  [app-state project-id story-id {:keys [title status]}]
  [:li.card
   (if (= status "done")
     [:del title]
     [:span
      title
      " "
      [:button
       {:on-click
        (fn done-click [_]
          (swap! app-state assoc-in [:projects project-id :stories story-id :status]
                 "done"))}
       "Done"]])])

(defn project-board
  [app-state project-id]
  (let [project (get-in @app-state [:projects project-id])]
    [:div
     [:h2 (get project :title)]
     (into [:ul]
       (for [[id story] (get project :stories)]
         [story-card app-state project-id id story]))]))



(defn whip-content
  [app-state]
  (into [:div]
    (for [[id _] (get @app-state :projects)]
      [project-board app-state id])))

(defn whip-main
  [app-state]
  [:div
   [:h1 "Whip project management tool"]
   [whip-content app-state]])


(reagent/render-component [whip-main app-state]
                          (. js/document (getElementById "app")))