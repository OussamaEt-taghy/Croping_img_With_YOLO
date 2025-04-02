import cv2
import numpy as np

# Charger le modèle EAST pré-entraîné
net = cv2.dnn.readNet("frozen_east_text_detection.pb")

def detect_text(image_path):
    img = cv2.imread(image_path)
    h, w = img.shape[:2]
    new_w, new_h = (320, 320)  # Taille recommandée
    blob = cv2.dnn.blobFromImage(img, 1.0, (new_w, new_h), (123.68, 116.78, 103.94), swapRB=True, crop=False)

    net.setInput(blob)
    scores, geometry = net.forward(["feature_fusion/Conv_7/Sigmoid", "feature_fusion/concat_3"])

    # Analyser les résultats
    for i in range(scores.shape[2]):
        confidence = scores[0, 0, i]
        if confidence > 0.5:  # Seulement les objets détectés avec assez de confiance
            x1, y1, x2, y2 = int(geometry[0, 0, i]), int(geometry[0, 1, i]), int(geometry[0, 2, i]), int(geometry[0, 3, i])
            cropped = img[y1:y2, x1:x2]
            cv2.imwrite("cropped_cin.jpg", cropped)
            print("CIN détectée et recadrée !")
            return cropped
    print("Aucune CIN détectée.")

detect_text("cin_sample.jpg")
