using UnityEngine;

public class Target : MonoBehaviour
{
    // Start is called before the first frame update

    /*Käyttö:

    Target target = hit.transform.GetComponent<Target>();
    kun skripti jonkun objektin osa

    missä:

    osumafunktion out on hit*/
    public float health = 100f;
    public void TakeDamage(float amount){
        health -= amount;
        if(health <= 0f)
        {
            Die();
        }
    }

    void Die(){
        Destroy(gameObject);
    }


}
